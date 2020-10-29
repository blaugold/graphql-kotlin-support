package com.gabrielterwesten.graphql.support.autoconfigure

import com.gabrielterwesten.graphql.support.exceptions.*
import graphql.execution.DataFetcherExceptionHandler
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DataFetcherExceptionHandler::class)
@AutoConfigureBefore(name = ["com.expediagroup.graphql.spring.GraphQLAutoConfiguration"])
class GraphQlSupportExceptionsAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  fun delegatingGraphQlExceptionHandler(
      exceptionResolver: ExceptionResolver?,
      exceptionObserver: GraphQlExceptionObserver?,
      graphQlErrorCreator: GraphQlErrorCreator
  ): DelegatingGraphQlExceptionHandler =
      DelegatingGraphQlExceptionHandler(
          exceptionResolver,
          exceptionObserver,
          graphQlErrorCreator,
      )

  @Bean
  @ConditionalOnMissingBean
  fun delegatingDataFetcherExceptionHandler(
      graphQlExceptionHandler: GraphQlExceptionHandler
  ): DataFetcherExceptionHandler = DelegatingDataFetcherExceptionHandler(graphQlExceptionHandler)

  @Bean
  @ConditionalOnMissingBean
  fun defaultDataFetcherExceptionLogger(): GraphQlExceptionObserver =
      DefaultGraphQlExceptionLogger()
}
