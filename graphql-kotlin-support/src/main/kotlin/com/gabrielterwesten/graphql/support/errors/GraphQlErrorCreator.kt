package com.gabrielterwesten.graphql.support.errors

import graphql.GraphQLError
import graphql.execution.DataFetcherExceptionHandlerParameters

/**
 * Creates [GraphQLError] s from an exception thrown during data fetching.
 *
 * The exception might have been processed by an [ExceptionResolver].
 */
interface GraphQlErrorCreator {
  fun create(
      exception: Throwable,
      handlerParameters: DataFetcherExceptionHandlerParameters,
  ): GraphQLError
}
