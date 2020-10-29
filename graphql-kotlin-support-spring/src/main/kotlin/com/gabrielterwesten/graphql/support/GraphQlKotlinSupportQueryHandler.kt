package com.gabrielterwesten.graphql.support

import com.expediagroup.graphql.spring.execution.GRAPHQL_CONTEXT_KEY
import com.expediagroup.graphql.spring.execution.QueryHandler
import com.expediagroup.graphql.spring.extensions.toExecutionInput
import com.expediagroup.graphql.spring.extensions.toGraphQLResponse
import com.expediagroup.graphql.types.GraphQLError
import com.expediagroup.graphql.types.GraphQLRequest
import com.expediagroup.graphql.types.GraphQLResponse
import com.expediagroup.graphql.types.SourceLocation
import com.gabrielterwesten.graphql.support.dataloader.DataLoaderRegistryFactory
import com.gabrielterwesten.graphql.support.exceptions.GraphQlExceptionHandler
import graphql.GraphQL
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.future.await
import kotlinx.coroutines.reactor.ReactorContext

open class GraphQlKotlinSupportQueryHandler(
    private val graphql: GraphQL,
    private val dataLoaderRegistryFactory: DataLoaderRegistryFactory? = null,
    private val exceptionHandler: GraphQlExceptionHandler,
) : QueryHandler {

  @Suppress("TooGenericExceptionCaught")
  @ExperimentalCoroutinesApi
  override suspend fun executeQuery(request: GraphQLRequest): GraphQLResponse<*> {
    val reactorContext = coroutineContext[ReactorContext]
    val graphQLContext = reactorContext?.context?.getOrDefault<Any>(GRAPHQL_CONTEXT_KEY, null)
    val input = request.toExecutionInput(graphQLContext, dataLoaderRegistryFactory?.generate())

    return try {
      graphql.executeAsync(input).await().toGraphQLResponse()
    } catch (exception: Exception) {
      val graphKotlinQLError = exceptionHandler.handle(exception)
      GraphQLResponse<Any?>(errors = listOf(graphKotlinQLError.toGraphQLKotlinType()))
    }
  }
}

private fun graphql.GraphQLError.toGraphQLKotlinType() =
    GraphQLError(
        this.message.orEmpty(),
        this.locations?.map { it.toGraphQLKotlinType() },
        this.path,
        this.extensions,
    )

private fun graphql.language.SourceLocation.toGraphQLKotlinType() =
    SourceLocation(this.line, this.column)
