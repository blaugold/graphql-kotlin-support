pluginManagement {
    plugins {
        kotlin("jvm") version "1.4.10"
        kotlin("kapt") version "1.4.10"
        kotlin("plugin.spring") version "1.4.10"
        id("com.diffplug.spotless") version "5.7.0"
        id("io.spring.dependency-management") version "1.0.6.RELEASE"
        id("org.springframework.boot") version "2.3.4.RELEASE"
        id("com.jfrog.bintray") version "1.8.5"
    }
}

rootProject.name = "graphql-kotlin-support"

include("graphql-kotlin-support")
include("graphql-kotlin-support-spring")
include("graphql-kotlin-support-autoconfigure")
include("graphql-kotlin-support-starter")
include("example")
