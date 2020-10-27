import com.diffplug.gradle.spotless.SpotlessExtension
import com.jfrog.bintray.gradle.BintrayExtension
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") apply false
    id("io.spring.dependency-management") apply false
    id("com.diffplug.spotless")
    id("com.jfrog.bintray") apply false
}

allprojects {
    apply(plugin = "com.diffplug.spotless")

    group = "com.gabrielterwesten"
    version = "1.0-SNAPSHOT"

    spotless {
        kotlinGradle {
            ktlint()
        }
    }
}

subprojects {
    apply(plugin = "maven-publish")
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

    the<SpotlessExtension>().apply {
        kotlin {
            ktfmt("0.18")
        }

        format("yaml") {
            target("**/*.ya?ml")
            prettier()
        }

        format("html") {
            target("**/*.html")
            prettier()
        }
    }

    val publishedPackages = listOf(
        "graphql-kotlin-support",
        "graphql-kotlin-support-spring",
        "graphql-kotlin-support-autoconfigure",
        "graphql-kotlin-support-starter"
    )

    if (publishedPackages.contains(name)) {
        apply(plugin = "com.jfrog.bintray")

        val packageVersion = version as String
        val repoName = name

        val sources = the<SourceSetContainer>()["main"].allSource

        val sourceJar = tasks.create<Jar>("sourceJar") {
            from(sources)
            archiveClassifier.set("sources")
        }

        the<PublishingExtension>().apply {
            publications {
                create<MavenPublication>("maven") {
                    from(components["kotlin"])
                    artifact(sourceJar)
                }
            }
        }

        the<BintrayExtension>().apply {
            user = System.getenv("BINTRAY_USER")
            key = System.getenv("BINTRAY_API_KEY")
            setProperty("publications", arrayOf("maven"))

            pkg.apply {
                repo = "maven"
                name = repoName
                userOrg = "gabriel-terwesten-oss"
                setProperty("licenses", arrayOf("MIT"))
                vcsUrl = "https://github.com/blaugold/graphql-kotlin-support"

                version.apply {
                    name = packageVersion
                }
            }
        }
    }
}

spotless {
    format("markdown") {
        target("*.md")
        prettier()
    }

    format("yaml") {
        target(".github/**/*.ya?ml")
        prettier()
    }
}
