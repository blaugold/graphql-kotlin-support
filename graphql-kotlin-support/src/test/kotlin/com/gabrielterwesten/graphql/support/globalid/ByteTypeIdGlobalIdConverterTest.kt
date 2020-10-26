package com.gabrielterwesten.graphql.support.globalid

import ch.tutteli.atrium.api.fluent.en_GB.messageContains
import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.fluent.en_GB.toThrow
import ch.tutteli.atrium.api.verbs.expect
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.util.*
import org.junit.jupiter.api.Test

internal class ByteTypeIdGlobalIdConverterTest {

  @Test
  fun `round trip with wrapped id type Long`() {
    val converter = setupConverter()

    val id = LongId(3546375)
    val encodedId = converter.encode(id)
    val decodedId = converter.decode<GlobalId<*>>(encodedId)

    expect(decodedId).toBe(id)
  }

  @Test
  fun `round trip with wrapped id type String`() {
    val converter = setupConverter()

    val id = StringId("test")
    val encodedId = converter.encode(id)
    val decodedId = converter.decode<GlobalId<*>>(encodedId)

    expect(decodedId).toBe(id)
  }

  // region Decoding

  @Test
  fun `should throw if expect type does not match type of encoded id`() {
    val converter = setupConverter()

    expect { converter.decode(converter.encode(LongId(0)), StringId::class) }
        .toThrow<InvalidGlobalIdException>()
        .messageContains("is not of expected type")
  }

  @Test
  fun `should throw if type id can not be found`() {
    val converterA = setupConverter(GlobalIdRegistrationRepository().apply { add<LongId>(0) })
    val converterB = setupConverter(GlobalIdRegistrationRepository())

    expect { converterB.decode<GlobalId<*>>(converterA.encode(LongId(0)), null) }
        .toThrow<InvalidGlobalIdException>()
        .messageContains("is not registered")
  }

  @Test
  fun `should throw if id can not be decoded as base64`() {
    val converter = setupConverter()

    expect { converter.decode<GlobalId<*>>("a") }
        .toThrow<InvalidGlobalIdException>()
        .messageContains("Could not decode id as base64")
  }

  @Test
  fun `should throw if id has less than 3 bytes`() {
    val converter = setupConverter()

    expect {
          converter.decode<GlobalId<*>>(Base64.getUrlEncoder().encodeToString(byteArrayOf(0, 0)))
        }
        .toThrow<InvalidGlobalIdException>()
        .messageContains("Id is not formatted correctly")
  }

  // endregion

  class LongId(id: Long) : GlobalId<Long>(id)

  class StringId(id: String) : GlobalId<String>(id)

  private fun setupConverter(
      registrationRepository: GlobalIdRegistrationRepository? = null,
  ): ByteTypeIdGlobalIdConverter {
    return ByteTypeIdGlobalIdConverter(
        registrationRepository = registrationRepository
                ?: GlobalIdRegistrationRepository().apply {
                  add<LongId>(0)
                  add<StringId>(1)
                },
        objectMapper = ObjectMapper().registerKotlinModule())
  }
}
