package com.gabrielterwesten.graphql.support.schemagen

import com.expediagroup.graphql.execution.KotlinDataFetcherFactoryProvider
import com.fasterxml.jackson.databind.ObjectMapper
import com.gabrielterwesten.graphql.support.globalid.GlobalIdConverter
import graphql.schema.DataFetcherFactory
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import org.springframework.context.ApplicationContext

class ReactorGlobalIdSpringDataFetcherFactoryProvider(
    private val objectMapper: ObjectMapper,
    private val applicationContext: ApplicationContext,
    private val globalIdConverter: GlobalIdConverter,
) : KotlinDataFetcherFactoryProvider {

  override fun functionDataFetcherFactory(
      target: Any?, kFunction: KFunction<*>
  ): DataFetcherFactory<Any?> =
      DataFetcherFactory<Any?> {
        ReactorSpringDataFetcher(target, kFunction, objectMapper, applicationContext)
      }

  override fun propertyDataFetcherFactory(
      kClass: KClass<*>, kProperty: KProperty<*>
  ): DataFetcherFactory<Any?> =
      DataFetcherFactory { GlobalIdPropertyDataFetcher(kProperty.name, globalIdConverter) }
}
