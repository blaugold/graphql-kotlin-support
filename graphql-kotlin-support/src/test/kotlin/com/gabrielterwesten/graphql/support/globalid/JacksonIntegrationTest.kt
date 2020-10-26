package com.gabrielterwesten.graphql.support.globalid

import ch.tutteli.atrium.api.fluent.en_GB.cause
import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.fluent.en_GB.toThrow
import ch.tutteli.atrium.api.verbs.expect
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Test

internal class JacksonIntegrationTest {

  @Test
  fun `end to end`() {
    val objectMapper = setupMapper()

    val id = TestIdA(1)
    val serializedId = objectMapper.writeValueAsString(id)

    expect(objectMapper.readValue(serializedId, GlobalId::class.java)).toBe(id)

    expect(objectMapper.readValue(serializedId, TestIdA::class.java)).toBe(id)

    val obj = TestObject(id)

    val serializedObj = objectMapper.writeValueAsString(obj)

    expect(objectMapper.readValue(serializedObj, TestObject::class.java)).toBe(obj)
  }

  @Test
  fun `should throw JsonMappingException when decoding fails`() {
    val objectMapper = setupMapper()

    val encodedId = objectMapper.writeValueAsString(TestIdA(0))

    expect { objectMapper.readValue(encodedId, TestIdB::class.java) }.toThrow<
        JsonMappingException> { cause<InvalidGlobalIdException>() }
  }

  @Test
  fun `should throw JsonMappingException when encoding fails`() {
    // Exception is triggered by missing registration through empty repository.
    val objectMapper = setupMapper(GlobalIdRegistrationRepository())

    expect { objectMapper.writeValueAsString(TestIdA(0)) }.toThrow<JsonMappingException> {
      cause<IllegalArgumentException>()
    }
  }

  private fun setupMapper(
      registrationRepository: GlobalIdRegistrationRepository? = null
  ): ObjectMapper {
    val objectMapper = ObjectMapper()
    val converter =
        ByteTypeIdGlobalIdConverter(
            registrationRepository = registrationRepository
                    ?: GlobalIdRegistrationRepository().apply {
                      add<TestIdA>(0)
                      add<TestIdB>(1)
                    },
            objectMapper = objectMapper,
        )

    objectMapper.registerKotlinModule()
    objectMapper.registerModule(GlobalIdModule { converter })
    return objectMapper
  }

  class TestIdA(id: Long) : GlobalId<Long>(id)

  class TestIdB(id: Long) : GlobalId<Long>(id)

  data class TestObject(val id: TestIdA)
}
