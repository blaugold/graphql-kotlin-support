package com.gabrielterwesten.graphql.support.globalid

import kotlin.reflect.KClass

/** An id for one of the registered subclasses of [GlobalId]. */
abstract class TypeId {

  /** The bytes used to encode this type id. */
  abstract val bytes: ByteArray

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as TypeId

    if (!bytes.contentEquals(other.bytes)) return false

    return true
  }

  override fun hashCode(): Int = bytes.contentHashCode()
}

/**
 * A [TypeId] which uses two [Byte] s to encode the type.
 *
 * This should be enough to allocate a unique type id for each id class, even in APIs with many
 * types, while keeping the encoded id small.
 */
class ByteTypeId(upper: Byte, lower: Byte) : TypeId() {
  constructor(lower: Byte) : this(0, lower)

  constructor(bytes: ByteArray) : this(bytes[0], bytes[1])

  override val bytes = byteArrayOf(upper, lower)

  override fun toString(): String = "ByteTypeId(${bytes.toList()})"
}

/**
 * A registration of a [GlobalId] subclass so that it can be encoded and decoded.
 *
 * [TypeId] s must be unique with in a [GlobalIdRegistrationRepository].
 */
data class GlobalIdRegistration(
    val typeId: TypeId,
    val type: KClass<out GlobalId<*>>,
)

/** A repository for [GlobalIdRegistration] s. */
class GlobalIdRegistrationRepository {
  private val _registrations = mutableListOf<GlobalIdRegistration>()

  /** All currently registered [GlobalIdRegistration]. */
  val registrations: List<GlobalIdRegistration>
    get() = _registrations

  /** Adds [registration] to [registrations]. */
  fun add(registration: GlobalIdRegistration) {
    require(
        _registrations.all { it.typeId != registration.typeId && it.type != registration.type }) {
      "A registration for the typeId or type has already been registered: $registration"
    }

    _registrations.add(registration)
  }

  /** Finds a registered [GlobalIdRegistration] by [typeId] or returns `null`. */
  fun findByTypeId(typeId: TypeId): GlobalIdRegistration? =
      _registrations.find { it.typeId == typeId }

  /** Finds a registered [GlobalIdRegistration] by [type] or returns `null`. */
  fun findByType(type: KClass<*>): GlobalIdRegistration? = _registrations.find { it.type == type }

  inline fun <reified T : GlobalId<*>> add(typeId: TypeId) =
      add(GlobalIdRegistration(typeId, T::class))

  inline fun <reified T : GlobalId<*>> add(typeId: Byte) =
      add(GlobalIdRegistration(ByteTypeId(typeId), T::class))
}
