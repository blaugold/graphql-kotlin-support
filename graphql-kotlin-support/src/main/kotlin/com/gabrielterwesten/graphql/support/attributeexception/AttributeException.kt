package com.gabrielterwesten.graphql.support.attributeexception

/** Configuration for the creation of attributes by an [AttributeException]. */
interface AttributeExceptionConfig {

  /** Whether to include the stack trace of the exception in the attributes. */
  val includeStackTrace: Boolean
}

data class DefaultAttributeConfig(override val includeStackTrace: Boolean = false) :
    AttributeExceptionConfig

/**
 * An exception which provides a method to extract attributes in the form of a [Map]. The usual
 * purpose of the returned attributes is to include them in the error response to a client.
 *
 * The creation of these attributes can be configured through the [AttributeExceptionConfig] passed
 * to [createAttributes].
 */
abstract class AttributeException : RuntimeException() {

  abstract override val message: String

  open fun createAttributes(config: AttributeExceptionConfig): Map<String, Any?> {
    val result = mutableMapOf<String, Any?>()

    if (config.includeStackTrace) {
      result["stackTrace"] = stackTraceToString().split("\n").map { it.replace("\t", "  ") }
    }

    return result
  }
}
