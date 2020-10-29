import com.diffplug.gradle.spotless.SpotlessExtension
import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.tasks.BintrayUploadTask
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.publish.maven.internal.artifact.FileBasedMavenArtifact
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
    version = "0.3.0"

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

    configure<DependencyManagementExtension> {
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

    configure<JavaPluginExtension> {
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "11"
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
    }

    configure<SpotlessExtension> {
        kotlin {
            ktfmt("0.18")
        }

        format("yaml") {
            target("src/**/*.yaml", "src/**/*.yml")
            prettier()
        }

        format("html") {
            target("src/**/*.html")
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
        val packageName = name
        val scmUrl = "https://github.com/blaugold/graphql-kotlin-support"

        val sources = the<SourceSetContainer>()["main"].allSource

        val sourceJar = tasks.create<Jar>("sourceJar") {
            from(sources)
            archiveClassifier.set("sources")
        }

        configure<PublishingExtension> {
            publications {
                create<MavenPublication>("maven") {
                    from(components["kotlin"])
                    artifact(sourceJar)
                    pom {
                        name.set(packageName)
                        description.set("Opinionated library to support the implementation of GraphQL APIs with graphql-kotlin.")
                        developers { developer { name.set("Gabriel Terwesten") } }
                        scm { url.set(scmUrl) }
                        licenses { license { name.set("MIT") } }
                    }
                }
            }
        }

        configure<BintrayExtension> {
            user = System.getenv("BINTRAY_USER")
            key = System.getenv("BINTRAY_API_KEY")
            setProperty("publications", arrayOf("maven"))
            publish = true

            pkg.apply {
                repo = "maven"
                name = packageName
                userOrg = "gabriel-terwesten-oss"
                setProperty("licenses", arrayOf("MIT"))
                vcsUrl = scmUrl

                version.apply {
                    name = packageVersion
                }
            }
        }

        val project = this

        tasks.withType<BintrayUploadTask> {
            doFirst {
                project.the<PublishingExtension>().publications
                    .filterIsInstance<MavenPublication>()
                    .forEach { publication ->
                        val moduleFile = buildDir.resolve("publications/${publication.name}/module.json")
                        if (moduleFile.exists()) {
                            publication.artifact(object : FileBasedMavenArtifact(moduleFile) {
                                override fun getDefaultExtension() = "module"
                            })
                        }
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
        target(".github/**/*.yaml", ".github/**/*.yml")
        prettier()
    }
}
