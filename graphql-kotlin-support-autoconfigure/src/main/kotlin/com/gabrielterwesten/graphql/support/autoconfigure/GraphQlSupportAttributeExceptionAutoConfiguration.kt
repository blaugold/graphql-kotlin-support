package com.gabrielterwesten.graphql.support.autoconfigure

import com.gabrielterwesten.graphql.support.attributeexception.AttributeException
import com.gabrielterwesten.graphql.support.attributeexception.AttributeExceptionConfig
import com.gabrielterwesten.graphql.support.attributeexception.AttributeExceptionGraphQlErrorCreator
import com.gabrielterwesten.graphql.support.attributeexception.DefaultAttributeConfig
import com.gabrielterwesten.graphql.support.errors.GraphQlErrorCreator
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.web.ErrorProperties
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(AttributeException::class)
class GraphQlSupportAttributeExceptionAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  fun attributeExceptionConfig(serverProperties: ServerProperties): AttributeExceptionConfig {
    val includeStackTrace =
        serverProperties.error.includeStacktrace == ErrorProperties.IncludeStacktrace.ALWAYS

    return DefaultAttributeConfig(includeStackTrace = includeStackTrace)
  }

  @Bean
  @ConditionalOnMissingBean
  fun attributeExceptionGraphQlErrorCreator(config: AttributeExceptionConfig): GraphQlErrorCreator =
      AttributeExceptionGraphQlErrorCreator(config)
}
