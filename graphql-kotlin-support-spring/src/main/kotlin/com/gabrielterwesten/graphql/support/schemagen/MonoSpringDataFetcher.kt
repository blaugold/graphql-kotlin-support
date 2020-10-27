package com.gabrielterwesten.graphql.support.schemagen

import com.expediagroup.graphql.spring.execution.SpringDataFetcher
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.schema.DataFetchingEnvironment
import kotlin.reflect.KFunction
import org.springframework.context.ApplicationContext
import reactor.core.publisher.Mono

class MonoSpringDataFetcher(
    target: Any?,
    fn: KFunction<*>,
    objectMapper: ObjectMapper,
    applicationContext: ApplicationContext,
) : SpringDataFetcher(target, fn, objectMapper, applicationContext) {
  override fun get(environment: DataFetchingEnvironment): Any? =
      when (val result = super.get(environment)
      ) {
        is Mono<*> -> result.toFuture()
        else -> result
      }
}
