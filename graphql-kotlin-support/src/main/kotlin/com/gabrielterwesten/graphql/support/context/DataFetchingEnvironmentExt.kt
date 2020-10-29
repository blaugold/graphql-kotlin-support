package com.gabrielterwesten.graphql.support.context

import graphql.schema.DataFetchingEnvironment
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import reactor.util.context.Context

fun DataFetchingEnvironment.getCoroutineContext(): CoroutineContext =
    (getContext<Any?>() as? CoroutineScope)?.coroutineContext ?: EmptyCoroutineContext

fun DataFetchingEnvironment.getReactorContext(): Context =
    (getContext<Any?>() as? ReactorContextProvider)?.reactorContext ?: Context.empty()
