package com.gabrielterwesten.graphql.support.example.graphql

import com.expediagroup.graphql.spring.operations.Query
import com.gabrielterwesten.graphql.support.example.domain.UserService
import com.gabrielterwesten.graphql.support.example.utils.convert
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Component

@Component
class UserQueries(
    private val userService: UserService,
    private val conversionService: ConversionService,
) : Query {

  fun allUsers(): List<GqlUser> = userService.listAllUsers().map(conversionService::convert)
}
