package com.gabrielterwesten.graphql.support.schemagen

import com.expediagroup.graphql.spring.execution.GraphQLContextFactory
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactor.ReactorContext
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import reactor.util.context.Context

open class ReactorGraphQlContextFactory : GraphQLContextFactory<ReactorGraphQlContext> {

  override suspend fun generateContext(
      request: ServerHttpRequest, response: ServerHttpResponse
  ): ReactorGraphQlContext = ReactorGraphQlContext(getReactorContext())

  @OptIn(ExperimentalCoroutinesApi::class)
  suspend fun getReactorContext(): Context? = coroutineContext[ReactorContext]?.context
}
