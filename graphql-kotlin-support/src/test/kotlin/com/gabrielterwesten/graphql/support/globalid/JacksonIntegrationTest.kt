package com.gabrielterwesten.graphql.support.globalid

import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.verbs.expect
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Test

internal class JacksonIntegrationTest {

  @Test
  fun `end to end`() {
    val objectMapper = ObjectMapper()
    val idConverter =
        ByteTypeIdGlobalIdConverter(
            registrationRepository = GlobalIdRegistrationRepository().apply { add<TestId>(0) },
            objectMapper = objectMapper,
        )

    objectMapper.registerKotlinModule()
    objectMapper.registerModule(GlobalIdModule { idConverter })

    val id = TestId(1)
    val serializedId = objectMapper.writeValueAsString(id)

    expect(objectMapper.readValue(serializedId, GlobalId::class.java)).toBe(id)

    expect(objectMapper.readValue(serializedId, TestId::class.java)).toBe(id)

    val obj = TestObject(id)

    val serializedObj = objectMapper.writeValueAsString(obj)

    expect(objectMapper.readValue(serializedObj, TestObject::class.java)).toBe(obj)
  }

  class TestId(id: Long) : GlobalId<Long>(id)

  data class TestObject(val id: TestId)
}
