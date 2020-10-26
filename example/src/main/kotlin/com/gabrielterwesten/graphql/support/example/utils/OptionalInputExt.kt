package com.gabrielterwesten.graphql.support.example.utils

import com.expediagroup.graphql.execution.OptionalInput

fun <T> OptionalInput<T>.orElse(value: T?): T? =
    when (this) {
      is OptionalInput.Defined -> this.value
      else -> value
    }
