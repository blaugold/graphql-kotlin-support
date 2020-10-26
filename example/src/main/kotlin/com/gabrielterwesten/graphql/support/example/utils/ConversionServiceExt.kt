package com.gabrielterwesten.graphql.support.example.utils

import org.springframework.core.convert.ConversionService

inline fun <reified T : Any> ConversionService.convert(value: Any): T =
    convert(value, T::class.java)!!
