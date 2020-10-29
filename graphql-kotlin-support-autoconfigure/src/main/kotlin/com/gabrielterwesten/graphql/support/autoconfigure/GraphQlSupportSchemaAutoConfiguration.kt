package com.gabrielterwesten.graphql.support.autoconfigure

import com.expediagroup.graphql.execution.KotlinDataFetcherFactoryProvider
import com.expediagroup.graphql.hooks.SchemaGeneratorHooks
import com.expediagroup.graphql.spring.execution.GraphQLContextFactory
import com.expediagroup.graphql.spring.execution.QueryHandler
import com.fasterxml.jackson.databind.ObjectMapper
import com.gabrielterwesten.graphql.support.GraphQlKotlinSupportContextFactory
import com.gabrielterwesten.graphql.support.GraphQlKotlinSupportDataFetcherFactoryProvider
import com.gabrielterwesten.graphql.support.GraphQlKotlinSupportQueryHandler
import com.gabrielterwesten.graphql.support.GraphQlKotlinSupportSchemaGenerationHooks
import com.gabrielterwesten.graphql.support.dataloader.DataLoaderRegistryFactory
import com.gabrielterwesten.graphql.support.exceptions.GraphQlExceptionHandler
import com.gabrielterwesten.graphql.support.globalid.GlobalId
import com.gabrielterwesten.graphql.support.globalid.GlobalIdConverter
import graphql.GraphQL
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Mono

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(SchemaGeneratorHooks::class, GlobalId::class, Mono::class)
@AutoConfigureBefore(name = ["com.expediagroup.graphql.spring.GraphQLAutoConfiguration"])
@AutoConfigureAfter(GraphQlSupportGlobalIdAutoConfiguration::class)
@ConditionalOnBean(GlobalIdConverter::class)
class GraphQlSupportSchemaAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  fun reactorGlobalIdSchemaGenerationHooks(): SchemaGeneratorHooks =
      GraphQlKotlinSupportSchemaGenerationHooks()

  @Bean
  @ConditionalOnMissingBean
  fun reactorGlobalIdSpringDataFetcherFactoryProvider(
      objectMapper: ObjectMapper,
      applicationContext: ApplicationContext,
      globalIdConverter: GlobalIdConverter,
  ): KotlinDataFetcherFactoryProvider =
      GraphQlKotlinSupportDataFetcherFactoryProvider(
          objectMapper,
          applicationContext,
          globalIdConverter,
      )

  @Bean
  @ConditionalOnMissingBean
  fun reactorGraphQlContextFactor(): GraphQLContextFactory<*> = GraphQlKotlinSupportContextFactory()

  @Bean
  @ConditionalOnMissingBean
  fun graphQlKotlinSupportQueryHandler(
      graphQl: GraphQL,
      dataLoaderRegistryFactory: DataLoaderRegistryFactory? = null,
      exceptionHandler: GraphQlExceptionHandler,
  ): QueryHandler =
      GraphQlKotlinSupportQueryHandler(
          graphQl,
          dataLoaderRegistryFactory,
          exceptionHandler,
      )
}
