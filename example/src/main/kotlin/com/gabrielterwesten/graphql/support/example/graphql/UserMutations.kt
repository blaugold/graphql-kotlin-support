package com.gabrielterwesten.graphql.support.example.graphql

import com.expediagroup.graphql.execution.OptionalInput
import com.expediagroup.graphql.spring.operations.Mutation
import com.gabrielterwesten.graphql.support.example.NotFoundException
import com.gabrielterwesten.graphql.support.example.domain.User
import com.gabrielterwesten.graphql.support.example.domain.UserService
import com.gabrielterwesten.graphql.support.example.utils.convert
import com.gabrielterwesten.graphql.support.example.utils.orElse
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Component

data class CreateUserInput(
    override val clientRequestId: String?,
    val userName: String,
    val displayName: String,
    val email: String?,
) : MutationInput

data class CreateUserPayload(
    override val clientRequestId: String?,
    val user: GqlUser,
) : MutationPayload

data class UpdateUserInput(
    override val clientRequestId: String?,
    val id: UserId,
    val userName: String?,
    val displayName: String?,
    val email: OptionalInput<String>,
) : MutationInput

data class UpdateUserPayload(
    override val clientRequestId: String?,
    val user: GqlUser,
) : MutationPayload

data class DeleteUserInput(
    override val clientRequestId: String?,
    val id: UserId,
) : MutationInput

data class DeleteUserPayload(
    override val clientRequestId: String?,
) : MutationPayload

@Component
class UserMutations(
    private val userService: UserService,
    private val conversionService: ConversionService,
) : Mutation {

  fun createUser(input: CreateUserInput): CreateUserPayload {
    val user =
        userService.createUser(
            User(userName = input.userName, displayName = input.displayName, email = input.email))

    return CreateUserPayload(input.clientRequestId, user.let(conversionService::convert))
  }

  fun updateUser(input: UpdateUserInput): UpdateUserPayload {
    val user = userService.findUserById(input.id.id) ?: throw NotFoundException()

    val updatedUser =
        user
            .run {
              copy(
                  userName = input.userName ?: userName,
                  displayName = input.displayName ?: displayName,
                  email = input.email.orElse(email),
              )
            }
            .let(userService::updateUser)

    return UpdateUserPayload(input.clientRequestId, updatedUser.let(conversionService::convert))
  }

  fun deleteUser(input: DeleteUserInput): DeleteUserPayload {
    userService.deleteUser(input.id.id)

    return DeleteUserPayload(input.clientRequestId)
  }
}
