package com.gabrielterwesten.graphql.support

import com.expediagroup.graphql.spring.execution.GRAPHQL_CONTEXT_KEY
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactor.ReactorContext

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T : Any> getGraphQlContextOrNull(): T? =
    coroutineContext[ReactorContext]?.context?.get(GRAPHQL_CONTEXT_KEY)

suspend fun <T : Any> getGraphQlContext(): T = getGraphQlContextOrNull()!!
