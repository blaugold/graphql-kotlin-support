package com.gabrielterwesten.graphql.support.example.graphql

import com.gabrielterwesten.graphql.support.errors.DataFetcherExceptionObserver
import com.gabrielterwesten.graphql.support.example.ApiException
import com.gabrielterwesten.graphql.support.example.InternalErrorException
import graphql.execution.DataFetcherExceptionHandlerParameters
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class DataFetcherExceptionLogger : DataFetcherExceptionObserver {

  private val logger = LoggerFactory.getLogger(this::class.java)

  override fun onException(
      exception: Throwable, handlerParameters: DataFetcherExceptionHandlerParameters
  ) {
    val context = handlerParameters.dataFetchingEnvironment.getContext<CustomGraphQlContext>()
    val exName = exception::class.simpleName
    when (exception) {
      is InternalErrorException ->
          logger.error("[${context.requestId}] Internal error: $exName", exception)
      is ApiException -> logger.info("[${context.requestId}] Handled exception: $exName", exception)
    }
  }
}
