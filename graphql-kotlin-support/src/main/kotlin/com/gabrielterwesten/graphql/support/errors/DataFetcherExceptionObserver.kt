package com.gabrielterwesten.graphql.support.errors

import graphql.execution.DataFetcherExceptionHandlerParameters

/**
 * An observer which is notified of every exception thrown during data fetching.
 *
 * The exception might have been processed by an [ExceptionResolver].
 */
interface DataFetcherExceptionObserver {
  fun onException(
      exception: Throwable,
      handlerParameters: DataFetcherExceptionHandlerParameters,
  )
}
