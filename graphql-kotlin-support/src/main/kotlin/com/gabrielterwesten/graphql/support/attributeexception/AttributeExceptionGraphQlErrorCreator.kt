package com.gabrielterwesten.graphql.support.attributeexception

import com.gabrielterwesten.graphql.support.errors.GraphQlErrorCreator
import graphql.ErrorClassification
import graphql.ErrorType
import graphql.GraphQLError
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.language.SourceLocation

class AttributeExceptionGraphQlErrorCreator(
    private val config: AttributeExceptionConfig,
) : GraphQlErrorCreator {
  override fun create(
      exception: Throwable,
      handlerParameters: DataFetcherExceptionHandlerParameters,
  ): GraphQLError {
    require(exception is AttributeException) {
      "${this::class.simpleName} can only handle AttributeExceptions but got ${exception::class}"
    }

    return AttributeExceptionGraphQLError(
        message = exception.message,
        path = handlerParameters.path.toList(),
        locations = listOf(handlerParameters.sourceLocation),
        extensions = exception.createAttributes(config),
    )
  }
}

class AttributeExceptionGraphQLError(
    private val message: String,
    private val path: List<Any>?,
    private val locations: List<SourceLocation>,
    private val extensions: Map<String, Any?>
) : GraphQLError {
  override fun getMessage(): String = message

  override fun getLocations(): List<SourceLocation> = locations

  override fun getErrorType(): ErrorClassification = ErrorType.DataFetchingException

  override fun getExtensions(): Map<String, Any?> = extensions

  override fun getPath(): List<Any>? = path
}
