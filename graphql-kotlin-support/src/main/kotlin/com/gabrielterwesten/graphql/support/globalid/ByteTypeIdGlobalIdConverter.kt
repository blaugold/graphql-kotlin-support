package com.gabrielterwesten.graphql.support.globalid

import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.javaType

/**
 * A [GlobalIdConverter] which expects [ByteTypeId] in all [GlobalIdRegistration] s.
 *
 * To encode an id, it serializes the wrapped [GlobalId.id] with the help of an [objectMapper],
 * concatenates the [TypeId.bytes] and the wrapped id into a [ByteArray] and encodes that as a
 * base64 string.
 */
class ByteTypeIdGlobalIdConverter(
    private val registrationRepository: GlobalIdRegistrationRepository,
    private val objectMapper: ObjectMapper,
) : GlobalIdConverter {

  private val b64encoder = Base64.getUrlEncoder()
  private val b64decoder = Base64.getUrlDecoder()

  init {
    require(registrationRepository.registrations.all { it.typeId is ByteTypeId }) {
      "DefaultGlobalIdConverter only supports ByteTypeId"
    }
  }

  override fun encode(id: GlobalId<*>): String {
    val registration = registrationRepository.findByType(id::class)
    requireNotNull(registration) { "${id::class} is not a registered GlobalId type" }

    val wrappedIdBytes = objectMapper.writeValueAsBytes(id.id)
    val bytes = registration.typeId.bytes + wrappedIdBytes

    return b64encoder.encodeToString(bytes)
  }

  @OptIn(ExperimentalStdlibApi::class)
  override fun <T : GlobalId<*>> decode(id: String, expectedType: KClass<T>?): T {
    val rawIdBytes =
        try {
          b64decoder.decode(id)
        } catch (e: Throwable) {
          throw InvalidGlobalIdException(
              encodedId = id,
              "Could not decode id as base64: $id",
              cause = e,
          )
        }

    // Verify that at least the two bytes of the TypeId and one byte from the wrapped id are there
    if (rawIdBytes.size < 3)
        throw InvalidGlobalIdException(
            encodedId = id,
            "Id is not formatted correctly: ${rawIdBytes.toList()}",
        )

    val typeIdBytes = rawIdBytes.sliceArray(0 until 2)
    val typeId = ByteTypeId(typeIdBytes)

    val registration =
        registrationRepository.findByTypeId(typeId)
        // Verify that type is registered
        ?: throw InvalidGlobalIdException(
                encodedId = id,
                "$typeId is not registered.",
            )

    // Verify that id is of expected type if requested
    if (expectedType != null && !expectedType.isSuperclassOf(registration.type))
        throw InvalidGlobalIdException(
            encodedId = id,
            "Id type (${registration.type}) is not of expected type (${expectedType}).",
        )

    val wrappedIdBytes = rawIdBytes.sliceArray(2 until rawIdBytes.size)
    val wrappedIdType =
        objectMapper.typeFactory.constructType(registration.type.wrappedIdType.javaType)
    val wrappedId = objectMapper.readValue<Any>(wrappedIdBytes, wrappedIdType)

    @Suppress("UNCHECKED_CAST")
    return registration.type.primaryConstructor!!.call(wrappedId) as T
  }
}
