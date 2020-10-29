package com.gabrielterwesten.graphql.support.exceptions

import graphql.GraphQLError
import graphql.execution.DataFetcherExceptionHandlerParameters

class DelegatingGraphQlExceptionHandler(
    private val exceptionResolver: ExceptionResolver? = null,
    private val exceptionObserver: GraphQlExceptionObserver? = null,
    private val graphQlErrorCreator: GraphQlErrorCreator
) : GraphQlExceptionHandler {
  override suspend fun handle(
      exception: Throwable,
      handlerParameters: DataFetcherExceptionHandlerParameters?,
  ): GraphQLError {
    val resolvedException = exceptionResolver?.resolveException(exception) ?: exception

    exceptionObserver?.also { it.onException(resolvedException, handlerParameters) }

    return graphQlErrorCreator.create(resolvedException, handlerParameters)
  }
}
