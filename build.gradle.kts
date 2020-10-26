import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") apply false
    id("io.spring.dependency-management") apply false
}

allprojects {
    group = "com.gabrielterwesten"
    version = "1.0-SNAPSHOT"
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        mavenCentral()
    }

    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:2.3.4.RELEASE")
        }

        dependencies {
            dependency("ch.tutteli.atrium:atrium-fluent-en_GB:0.13.0")
            dependencySet("com.expediagroup:4.0.0-alpha.6") {
                entry("graphql-kotlin-spring-server")
                entry("graphql-kotlin-spring-client")
            }
            dependency("com.fasterxml.jackson.core:jackson-databind:2.11.1")
            dependency("com.graphql-java:graphql-java:15.0")
            dependency("org.junit.jupiter:junit-jupiter:5.7.0")
        }
    }

    the<JavaPluginExtension>().targetCompatibility = JavaVersion.VERSION_11

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "11"
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
    }
}
