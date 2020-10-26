plugins {
    kotlin("kapt")
}

dependencies {
    implementation("com.graphql-java:graphql-java")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    compileOnly(project(":graphql-kotlin-support"))
    kapt("org.springframework.boot:spring-boot-autoconfigure-processor")
}
