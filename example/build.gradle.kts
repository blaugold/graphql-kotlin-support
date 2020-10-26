plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("com.expediagroup:graphql-kotlin-spring-server")
    implementation(project(":graphql-kotlin-support-starter"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
