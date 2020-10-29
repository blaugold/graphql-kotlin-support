package com.gabrielterwesten.graphql.support.example.graphql

import com.gabrielterwesten.graphql.support.GraphQlKotlinSupportContext
import com.gabrielterwesten.graphql.support.GraphQlKotlinSupportContextFactory
import com.gabrielterwesten.graphql.support.getGraphQlContext
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import reactor.util.context.Context

data class CustomGraphQlContext(val requestId: String, val context: Context?) :
    GraphQlKotlinSupportContext(context)

suspend fun getContext() = getGraphQlContext<CustomGraphQlContext>()

@Component
class CustomGraphQlContextFactory : GraphQlKotlinSupportContextFactory() {
  override suspend fun generateContext(
      request: ServerHttpRequest, response: ServerHttpResponse
  ): CustomGraphQlContext = CustomGraphQlContext(requestId = request.id, getReactorContext())
}
