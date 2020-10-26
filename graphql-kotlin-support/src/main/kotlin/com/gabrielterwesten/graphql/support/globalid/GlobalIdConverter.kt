package com.gabrielterwesten.graphql.support.globalid

import kotlin.reflect.KClass

/** A converter which encodes and decodes [GlobalId] s from and to [String] s. */
interface GlobalIdConverter {

  /** Encodes [id] into a [String]. */
  fun encode(id: GlobalId<*>): String

  /**
   * Decodes [id] into a [GlobalId].
   *
   * If [expectedType] is passed, the returned value will be of that type. If the [id] cannot be
   * decoded into that type, a [InvalidGlobalIdException] is thrown.
   */
  fun <T : GlobalId<*>> decode(id: String, expectedType: KClass<T>? = null): T
}
