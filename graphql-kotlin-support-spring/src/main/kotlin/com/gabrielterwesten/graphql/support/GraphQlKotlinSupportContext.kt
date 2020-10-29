package com.gabrielterwesten.graphql.support

import com.expediagroup.graphql.execution.GraphQLContext
import com.expediagroup.graphql.spring.execution.GRAPHQL_CONTEXT_KEY
import com.gabrielterwesten.graphql.support.context.ReactorContextProvider
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactor.asCoroutineContext
import reactor.util.context.Context

open class GraphQlKotlinSupportContext(context: Context? = null) :
    GraphQLContext, ReactorContextProvider, CoroutineScope {

  private val originalContext = context

  override val reactorContext: Context by lazy {
    (originalContext ?: Context.empty()).put(GRAPHQL_CONTEXT_KEY, this)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  override val coroutineContext: CoroutineContext by lazy { reactorContext.asCoroutineContext() }
}
