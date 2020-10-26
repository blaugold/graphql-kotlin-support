package com.gabrielterwesten.graphql.support.errors

import graphql.execution.DataFetcherExceptionHandlerParameters
import org.slf4j.LoggerFactory

class DefaultDataFetcherExceptionLogger : DataFetcherExceptionObserver {

  private val logger = LoggerFactory.getLogger(this::class.java)

  override fun onException(
      exception: Throwable, handlerParameters: DataFetcherExceptionHandlerParameters
  ) {
    logger.warn("Exception during data fetching.", exception)
  }
}
