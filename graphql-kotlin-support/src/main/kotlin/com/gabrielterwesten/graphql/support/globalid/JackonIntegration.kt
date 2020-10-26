package com.gabrielterwesten.graphql.support.globalid

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

/**
 * [GlobalIdConverter] may depend on [ObjectMapper]. This provider allows registration of
 * [GlobalIdModule] before the converter has been created.
 */
fun interface GlobalIdConverterProvider {
  fun get(): GlobalIdConverter
}

/** Serializer which uses [GlobalIdConverter] to serialize a [GlobalId] as a string. */
class GlobalIdSerializer(private val globalIdConverterProvider: GlobalIdConverterProvider) :
    StdSerializer<GlobalId<*>>(GlobalId::class.java) {

  override fun serialize(value: GlobalId<*>, gen: JsonGenerator, provider: SerializerProvider) {
    try {
      gen.writeString(globalIdConverterProvider.get().encode(value))
    } catch (e: Throwable) {
      throw JsonMappingException.from(gen, "Could not encode GlobalId.", e)
    }
  }
}

/**
 * Deserializer which uses a [GlobalIdConverter] to deserialize a string into a [GlobalId]. The
 * string will be serialized into an instance of [idType], if not `null`.
 */
class GlobalIdDeserializer(
    private val globalIdConverterProvider: GlobalIdConverterProvider,
    private val idType: KClass<GlobalId<*>>? = null,
) : StdDeserializer<GlobalId<*>>(GlobalId::class.java), ContextualDeserializer {

  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): GlobalId<*> =
      try {
        globalIdConverterProvider.get().decode(p.valueAsString, idType)
      } catch (e: Throwable) {
        throw JsonMappingException.from(p, "Could not decode GlobalId.", e)
      }

  @Suppress("UNCHECKED_CAST")
  override fun createContextual(
      ctxt: DeserializationContext, property: BeanProperty?
  ): JsonDeserializer<*> =
      GlobalIdDeserializer(
          globalIdConverterProvider,
          ctxt.contextualType.rawClass.kotlin as KClass<GlobalId<*>>,
      )
}

/**
 * Module which adds [GlobalIdSerializer], [GlobalIdDeserializer] and handles serialization and
 * deserialization of all subclasses of [GlobalId].
 */
class GlobalIdModule(globalIdConverter: GlobalIdConverterProvider) : SimpleModule("GlobalId") {

  init {
    val globalIdSerializer = GlobalIdSerializer(globalIdConverter)
    val globalIdDeserializer = GlobalIdDeserializer(globalIdConverter)

    addSerializer(globalIdSerializer)
    addDeserializer(GlobalId::class.java, globalIdDeserializer)

    setSerializerModifier(
        object : BeanSerializerModifier() {
          override fun modifySerializer(
              config: SerializationConfig, beanDesc: BeanDescription, serializer: JsonSerializer<*>
          ): JsonSerializer<*> =
              when {
                // Install serializer for all subclasses of GlobalId
                GlobalId::class.isSuperclassOf(beanDesc.beanClass.kotlin) -> globalIdSerializer
                else -> serializer
              }
        },
    )

    setDeserializerModifier(
        object : BeanDeserializerModifier() {
          override fun modifyDeserializer(
              config: DeserializationConfig,
              beanDesc: BeanDescription,
              deserializer: JsonDeserializer<*>
          ): JsonDeserializer<*> =
              when {
                // Install deserializer for all subclasses of GlobalId
                GlobalId::class.isSuperclassOf(beanDesc.beanClass.kotlin) -> globalIdDeserializer
                else -> deserializer
              }
        },
    )
  }
}
