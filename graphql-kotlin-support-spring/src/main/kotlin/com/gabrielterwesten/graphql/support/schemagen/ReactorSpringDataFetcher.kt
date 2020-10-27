package com.gabrielterwesten.graphql.support.schemagen

import com.expediagroup.graphql.spring.execution.SpringDataFetcher
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.schema.DataFetchingEnvironment
import kotlin.reflect.KFunction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactor.asCoroutineContext
import org.springframework.context.ApplicationContext
import reactor.core.publisher.Mono
import reactor.util.context.Context

class ReactorSpringDataFetcher(
    private val target: Any?,
    private val fn: KFunction<*>,
    objectMapper: ObjectMapper,
    applicationContext: ApplicationContext,
) : SpringDataFetcher(target, fn, objectMapper, applicationContext) {

  @OptIn(ExperimentalCoroutinesApi::class)
  override fun get(environment: DataFetchingEnvironment): Any? {
    val reactorContext =
        (environment.getContext<Any>() as? ReactorGraphQlContext)?.context ?: Context.empty()

    val instance = target ?: environment.getSource()

    return instance
        ?.let {
          val parameterValues = getParameterValues(fn, environment)

          if (fn.isSuspend) {
            runSuspendingFunction(it, parameterValues, reactorContext.asCoroutineContext())
          } else {
            runBlockingFunction(it, parameterValues)
          }
        }
        ?.let {
          when (it) {
            is Mono<*> -> it.subscriberContext(reactorContext).toFuture()
            else -> it
          }
        }
  }
}
