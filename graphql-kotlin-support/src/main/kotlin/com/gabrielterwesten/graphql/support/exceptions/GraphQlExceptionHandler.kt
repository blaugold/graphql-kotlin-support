package com.gabrielterwesten.graphql.support.exceptions

import graphql.GraphQLError
import graphql.execution.DataFetcherExceptionHandlerParameters

interface GraphQlExceptionHandler {
  suspend fun handle(
      exception: Throwable,
      handlerParameters: DataFetcherExceptionHandlerParameters? = null,
  ): GraphQLError
}
