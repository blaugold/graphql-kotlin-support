package com.gabrielterwesten.graphql.support.attributeexception

interface AttributeConfig {
  val includeStackTrace: Boolean
}

data class DefaultAttributeConfig(override val includeStackTrace: Boolean = false) :
    AttributeConfig

abstract class AttributeException : RuntimeException() {

  abstract override val message: String

  open fun attributes(config: AttributeConfig): Map<String, Any?> {
    val result = mutableMapOf<String, Any?>()

    if (config.includeStackTrace) {
      result["stackTrace"] = stackTraceToString().split("\n").map { it.replace("\t", "  ") }
    }

    return result
  }
}
