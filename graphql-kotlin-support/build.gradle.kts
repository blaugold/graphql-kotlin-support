import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

dependencies {
    implementation("com.graphql-java:graphql-java")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation(kotlin("reflect"))

    testImplementation("ch.tutteli.atrium:atrium-fluent-en_GB")
    testImplementation("org.junit.jupiter:junit-jupiter") {
        exclude(group = "junit", module = "junit")
    }
}

spotless {
    kotlin {
        // TODO Waiting for support for kotlin 1.4 https://github.com/facebookincubator/ktfmt/issues/59
        targetExclude("src/main/kotlin/com/gabrielterwesten/graphql/support/globalid/JackonIntegration.kt")
    }
}

tasks.withType<KotlinCompile>() {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
}
