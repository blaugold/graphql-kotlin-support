package com.gabrielterwesten.graphql.support.example.graphql

import com.expediagroup.graphql.execution.GraphQLContext
import com.expediagroup.graphql.spring.execution.GraphQLContextFactory
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component

data class CustomGraphQlContext(val requestId: String) : GraphQLContext

@Component
class CustomGraphQlContextFactory : GraphQLContextFactory<CustomGraphQlContext> {
  override suspend fun generateContext(
      request: ServerHttpRequest, response: ServerHttpResponse
  ): CustomGraphQlContext = CustomGraphQlContext(requestId = request.id)
}
