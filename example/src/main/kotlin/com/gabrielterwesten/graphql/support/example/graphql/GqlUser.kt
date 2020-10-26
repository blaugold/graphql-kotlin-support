package com.gabrielterwesten.graphql.support.example.graphql

import com.expediagroup.graphql.annotations.GraphQLName
import com.gabrielterwesten.graphql.support.example.domain.User
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

class UserId(id: Long) : NodeId<Long>(id)

@GraphQLName("User")
data class GqlUser(
    override val id: UserId,
    val userName: String,
    val displayName: String,
    val email: String?,
) : Node

@Component
class GqlUserConverter : Converter<User, GqlUser> {
  override fun convert(user: User): GqlUser =
      GqlUser(
          id = UserId(user.id!!),
          userName = user.userName,
          displayName = user.displayName,
          email = user.email,
      )
}
