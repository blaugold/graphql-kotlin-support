package com.gabrielterwesten.graphql.support.globalid

/** Superclass for all global ids. */
abstract class GlobalId<T>(
    /** The id of the entity identified by this id. */
    val id: T
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as GlobalId<*>

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int = id?.hashCode() ?: 0

  override fun toString(): String = "${this::class.simpleName}($id)"
}
