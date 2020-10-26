package com.gabrielterwesten.graphql.support.example.graphql

import com.expediagroup.graphql.annotations.GraphQLDescription
import com.expediagroup.graphql.spring.operations.Query
import com.gabrielterwesten.graphql.support.example.InternalErrorException
import com.gabrielterwesten.graphql.support.example.NotFoundException
import com.gabrielterwesten.graphql.support.example.domain.UserService
import com.gabrielterwesten.graphql.support.example.utils.convert
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Component

@Component
class NodeQuery(
    private val userService: UserService,
    private val conversionService: ConversionService,
) : Query {

  @GraphQLDescription("Returns the `Node` with the given id.")
  fun node(id: NodeId<*>): Node =
      when (id) {
        is UserId -> userService.findUserById(id.id)?.let(conversionService::convert)
        else -> throw InternalErrorException("Resolution of $id is not implemented.")
      }
          ?: throw NotFoundException()
}
