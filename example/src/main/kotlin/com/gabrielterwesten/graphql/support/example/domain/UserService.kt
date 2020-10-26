package com.gabrielterwesten.graphql.support.example.domain

import com.gabrielterwesten.graphql.support.example.NotFoundException
import com.gabrielterwesten.graphql.support.example.UserNameAlreadyTakenException
import java.time.Instant
import kotlin.random.Random
import org.springframework.stereotype.Service

@Service
class UserService {
  private val users = mutableListOf<User>()

  fun createUser(user: User): User {
    validateUserName(user)

    val newUser =
        user.copy(
            id = Random.nextLong(),
            createdAt = Instant.now(),
            lastModifiedAt = null,
        )

    users.add(newUser)

    return newUser
  }

  fun updateUser(user: User): User {
    validateUserName(user)

    val updatedUser =
        (findUserById(user.id!!) ?: throw NotFoundException()).copy(
            lastModifiedAt = Instant.now(),
            userName = user.userName,
            displayName = user.displayName,
            email = user.email,
        )

    users.replaceAll { if (it.id == user.id) updatedUser else it }

    return updatedUser
  }

  fun deleteUser(id: Long) {
    users.removeIf { it.id == id }
  }

  fun findUserById(id: Long): User? = users.find { it.id == id }

  fun listAllUsers(): List<User> = users

  private fun validateUserName(user: User) {
    if (users.any { it.id != user.id && it.userName == user.userName })
        throw UserNameAlreadyTakenException(user.userName)
  }
}
