package com.gabrielterwesten.graphql.support.autoconfigure

import com.fasterxml.jackson.databind.ObjectMapper
import com.gabrielterwesten.graphql.support.globalid.*
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(GlobalId::class)
class GraphQlSupportGlobalIdAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(GlobalIdRegistrationRepository::class)
  fun byteTypeIdGlobalIdConverter(
      registrationRepository: GlobalIdRegistrationRepository,
      objectMapper: ObjectMapper,
  ): GlobalIdConverter = ByteTypeIdGlobalIdConverter(registrationRepository, objectMapper)

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(GlobalIdConverter::class)
  fun globalIdModule(applicationContext: ApplicationContext): GlobalIdModule =
      GlobalIdModule { applicationContext.getBean(GlobalIdConverter::class.java) }
}
