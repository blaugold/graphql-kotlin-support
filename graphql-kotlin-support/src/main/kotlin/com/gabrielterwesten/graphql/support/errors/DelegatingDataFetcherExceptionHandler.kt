package com.gabrielterwesten.graphql.support.errors

import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult

class DelegatingDataFetcherExceptionHandler(
    private val exceptionResolver: ExceptionResolver? = null,
    private val exceptionObserver: DataFetcherExceptionObserver? = null,
    private val graphQlErrorCreator: GraphQlErrorCreator
) : DataFetcherExceptionHandler {

  override fun onException(
      handlerParameters: DataFetcherExceptionHandlerParameters
  ): DataFetcherExceptionHandlerResult {
    val exception = handlerParameters.exception
    val resolvedException = exceptionResolver?.resolveException(exception) ?: exception

    exceptionObserver?.also { it.onException(resolvedException, handlerParameters) }

    val graphQLError = graphQlErrorCreator.create(resolvedException, handlerParameters)

    return DataFetcherExceptionHandlerResult.newResult(graphQLError).build()
  }
}
