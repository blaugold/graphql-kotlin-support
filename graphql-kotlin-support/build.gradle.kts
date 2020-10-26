
dependencies {
    implementation("com.graphql-java:graphql-java")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation(kotlin("reflect"))

    testImplementation("ch.tutteli.atrium:atrium-fluent-en_GB")
    testImplementation("org.junit.jupiter:junit-jupiter")
}
