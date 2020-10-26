package com.gabrielterwesten.graphql.support.example.graphql

import com.expediagroup.graphql.execution.KotlinDataFetcherFactoryProvider
import com.expediagroup.graphql.hooks.SchemaGeneratorHooks
import com.expediagroup.graphql.spring.execution.SpringDataFetcher
import com.fasterxml.jackson.databind.ObjectMapper
import com.gabrielterwesten.graphql.support.globalid.GlobalId
import com.gabrielterwesten.graphql.support.globalid.GlobalIdConverter
import com.gabrielterwesten.graphql.support.globalid.isGlobalId
import graphql.Scalars
import graphql.schema.DataFetcherFactory
import graphql.schema.DataFetchingEnvironment
import graphql.schema.GraphQLType
import graphql.schema.PropertyDataFetcher
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.KType

@Component
class CustomSchemaGeneration : SchemaGeneratorHooks {
    override fun willResolveMonad(type: KType): KType =
        when (type.classifier) {
            Mono::class -> type.arguments.firstOrNull()?.type
            else -> type
        }
            ?: type

    override fun willGenerateGraphQLType(type: KType): GraphQLType? =
        when {
            type.isGlobalId -> Scalars.GraphQLID
            else -> null
        }
}

class CustomFunctionDataFetcher(
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

class CustomPropertyDataFetcher(
    propertyName: String,
    private val globalIdConverter: GlobalIdConverter,
) : PropertyDataFetcher<Any?>(propertyName) {
    override fun get(environment: DataFetchingEnvironment?): Any? =
        when (val result = super.get(environment)
            ) {
            is GlobalId<*> -> globalIdConverter.encode(result)
            else -> result
        }
}

@Component
class CustomDataFetcherFactoryProvider(
    private val objectMapper: ObjectMapper,
    private val applicationContext: ApplicationContext,
    private val globalIdConverter: GlobalIdConverter,
) : KotlinDataFetcherFactoryProvider {

    override fun functionDataFetcherFactory(
        target: Any?, kFunction: KFunction<*>
    ): DataFetcherFactory<Any?> =
        DataFetcherFactory<Any?> {
            CustomFunctionDataFetcher(target, kFunction, objectMapper, applicationContext)
        }

    override fun propertyDataFetcherFactory(
        kClass: KClass<*>, kProperty: KProperty<*>
    ): DataFetcherFactory<Any?> =
        DataFetcherFactory { CustomPropertyDataFetcher(kProperty.name, globalIdConverter) }
}
