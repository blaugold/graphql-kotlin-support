package com.gabrielterwesten.graphql.support.exceptions

import graphql.execution.DataFetcherExceptionHandlerParameters
import org.slf4j.LoggerFactory

class DefaultGraphQlExceptionLogger : GraphQlExceptionObserver {

  private val logger = LoggerFactory.getLogger(this::class.java)

  override suspend fun onException(
      exception: Throwable,
      handlerParameters: DataFetcherExceptionHandlerParameters?,
  ) {
    logger.warn("Exception during data fetching.", exception)
  }
}
