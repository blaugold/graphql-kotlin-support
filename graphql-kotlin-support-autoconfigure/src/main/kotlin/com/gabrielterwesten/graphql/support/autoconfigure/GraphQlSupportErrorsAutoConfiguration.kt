package com.gabrielterwesten.graphql.support.autoconfigure

import com.gabrielterwesten.graphql.support.errors.*
import graphql.execution.DataFetcherExceptionHandler
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DataFetcherExceptionHandler::class)
@AutoConfigureBefore(name = ["com.expediagroup.graphql.spring.GraphQLAutoConfiguration"])
class GraphQlSupportErrorsAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  fun delegatingDataFetcherExceptionHandler(
      exceptionResolver: ExceptionResolver?,
      exceptionObserver: DataFetcherExceptionObserver?,
      graphQlErrorCreator: GraphQlErrorCreator
  ): DataFetcherExceptionHandler =
      DelegatingDataFetcherExceptionHandler(
          exceptionResolver,
          exceptionObserver,
          graphQlErrorCreator,
      )

  @Bean
  @ConditionalOnMissingBean
  fun defaultDataFetcherExceptionLogger(): DataFetcherExceptionObserver =
      DefaultDataFetcherExceptionLogger()
}
