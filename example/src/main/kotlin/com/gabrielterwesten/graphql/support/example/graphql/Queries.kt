package com.gabrielterwesten.graphql.support.example.graphql

import com.expediagroup.graphql.spring.operations.Query
import com.gabrielterwesten.graphql.support.example.InternalErrorException
import org.springframework.stereotype.Component

@Component
class Queries : Query {
  fun node(id: NodeId<*>): Node {
    when (id) {
      is UserId -> TODO("userService")
      else -> throw InternalErrorException("Resolution of $id is not implemented.")
    }
  }
}
