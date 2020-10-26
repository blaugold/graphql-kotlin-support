package com.gabrielterwesten.graphql.support.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication class Application

fun main(args: Array<String>) {
  runApplication<Application>()
}
