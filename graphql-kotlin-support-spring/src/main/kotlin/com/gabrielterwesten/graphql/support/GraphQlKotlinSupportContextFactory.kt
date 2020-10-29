package com.gabrielterwesten.graphql.support

import com.expediagroup.graphql.spring.execution.GraphQLContextFactory
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactor.ReactorContext
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import reactor.util.context.Context

open class GraphQlKotlinSupportContextFactory : GraphQLContextFactory<GraphQlKotlinSupportContext> {

  override suspend fun generateContext(
      request: ServerHttpRequest, response: ServerHttpResponse
  ): GraphQlKotlinSupportContext = GraphQlKotlinSupportContext(getReactorContext())

  @OptIn(ExperimentalCoroutinesApi::class)
  protected suspend fun getReactorContext(): Context? = coroutineContext[ReactorContext]?.context
}
