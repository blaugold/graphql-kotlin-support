package com.gabrielterwesten.graphql.support.example.domain

import java.time.Instant

data class User(
    val id: Long? = null,
    val createdAt: Instant = Instant.now(),
    val lastModifiedAt: Instant? = null,
    val userName: String,
    val displayName: String,
    val email: String? = null,
)
