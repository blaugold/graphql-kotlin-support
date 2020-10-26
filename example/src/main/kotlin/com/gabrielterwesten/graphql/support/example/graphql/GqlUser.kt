package com.gabrielterwesten.graphql.support.example.graphql

import com.expediagroup.graphql.annotations.GraphQLName

class UserId(id: Long) : NodeId<Long>(id)

@GraphQLName("User")
data class GqlUser(
    override val id: UserId,
    val userName: String,
    val displayName: String,
    val email: String?,
) : Node
