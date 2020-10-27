plugins {
    kotlin("kapt")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    compileOnly("com.graphql-java:graphql-java")
    compileOnly("com.fasterxml.jackson.core:jackson-databind")
    compileOnly("com.expediagroup:graphql-kotlin-spring-server")
    compileOnly(project(":graphql-kotlin-support"))
    compileOnly(project(":graphql-kotlin-support-spring"))
    kapt("org.springframework.boot:spring-boot-autoconfigure-processor")
}
