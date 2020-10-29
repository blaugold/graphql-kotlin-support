package com.gabrielterwesten.graphql.support.exceptions

import com.gabrielterwesten.graphql.support.context.getCoroutineContext
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import kotlinx.coroutines.runBlocking

class DelegatingDataFetcherExceptionHandler(
    private val exceptionHandler: GraphQlExceptionHandler,
) : DataFetcherExceptionHandler {

  override fun onException(
      handlerParameters: DataFetcherExceptionHandlerParameters
  ): DataFetcherExceptionHandlerResult {
    val error =
        runBlocking(handlerParameters.dataFetchingEnvironment.getCoroutineContext()) {
          exceptionHandler.handle(handlerParameters.exception, handlerParameters)
        }

    return DataFetcherExceptionHandlerResult.newResult(error).build()
  }
}
