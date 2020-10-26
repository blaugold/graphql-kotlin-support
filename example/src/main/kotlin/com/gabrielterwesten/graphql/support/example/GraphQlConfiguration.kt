package com.gabrielterwesten.graphql.support.example

import com.gabrielterwesten.graphql.support.example.graphql.UserId
import com.gabrielterwesten.graphql.support.globalid.GlobalIdRegistrationRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GraphQlConfiguration {

  @Bean
  fun globalIdRegistrationRepository() =
      GlobalIdRegistrationRepository().apply { add<UserId>(0) }
}
