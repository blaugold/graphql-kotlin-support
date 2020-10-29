package com.gabrielterwesten.graphql.support

import com.expediagroup.graphql.spring.execution.SpringDataFetcher
import com.fasterxml.jackson.databind.ObjectMapper
import com.gabrielterwesten.graphql.support.context.getCoroutineContext
import com.gabrielterwesten.graphql.support.context.getReactorContext
import graphql.schema.DataFetchingEnvironment
import kotlin.reflect.KFunction
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.context.ApplicationContext
import reactor.core.publisher.Mono

class GraphQlKotlinSupportDataFetcher(
    private val target: Any?,
    private val fn: KFunction<*>,
    objectMapper: ObjectMapper,
    applicationContext: ApplicationContext,
) : SpringDataFetcher(target, fn, objectMapper, applicationContext) {

  @OptIn(ExperimentalCoroutinesApi::class)
  override fun get(environment: DataFetchingEnvironment): Any? {
    val instance = target ?: environment.getSource()

    return instance
        ?.let {
          val parameterValues = getParameterValues(fn, environment)

          if (fn.isSuspend) {
            runSuspendingFunction(
                it,
                parameterValues,
                environment.getCoroutineContext(),
                // For suspending functions to work with DataLoaderDispatcherInstrumentation they
                // need to run before this DataFetcher returns. Otherwise the DataLoaders may be
                // dispatched before the suspending function has a chance to call DataLoader.load.
                CoroutineStart.UNDISPATCHED,
            )
          } else {
            runBlockingFunction(it, parameterValues)
          }
        }
        .let { transformResult(it, environment) }
  }

  private fun transformResult(result: Any?, environment: DataFetchingEnvironment): Any? =
      when (result) {
        is Mono<*> -> result.subscriberContext(environment.getReactorContext()).toFuture()
        else -> result
      }
}
