import com.gradle.publish.PublishTask

plugins {
    `java-gradle-plugin`
    signing
    `maven-publish`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.multiJvmTesting)
    alias(libs.plugins.gitSemVer)
    alias(libs.plugins.gradlePluginPublish)
    alias(libs.plugins.kotlin.qa)
    alias(libs.plugins.dokka)
    alias(libs.plugins.taskTree)
}

group = "io.github.spe.unibo"

repositories {
    mavenCentral()
}

gitSemVer {
    buildMetadataSeparator = "-"
}

multiJvm {
    maximumSupportedJvmVersion.set(latestJavaSupportedByGradle)
}

dependencies {
    api(gradleApi())
    api(gradleKotlinDsl())
    api(kotlin("stdlib-jdk8"))
    testImplementation(gradleTestKit())
    testImplementation(libs.konf.yaml)
    testImplementation(libs.classgraph)
    testImplementation(libs.bundles.kotlin.testing)
}

kotlin {
    compilerOptions {
        allWarningsAsErrors = true
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        showCauses = true
        showStackTraces = true
        events(*org.gradle.api.tasks.testing.logging.TestLogEvent.values())
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

gradlePlugin {
    plugins {
        website.set("https://unibo-spe.github.io")
        vcsUrl.set("https://github.com/unibo-spe/unpublished-plugin")
        create("") {
            id = "$group.${project.name}"
            displayName = "SPE test plugin"
            description = "Just a test plugin"
            implementationClass = "$group.template.HelloGradle"
            tags = listOf("unibo", "spe", "software process engineering", "teaching", "educational")
        }
    }
}

val localRepo = project.layout.buildDirectory.dir("myublish").get().asFile

publishing {
    repositories {
        maven {
            name = "MyRepo"
            url = localRepo.toURI()
        }
    }
    publications {
        create<MavenPublication>("Pluto") {
            from(components["java"])
            pom {
                name.set("SPE test plugin")
                description.set("Just a test plugin")
                url.set("https://unibo-spe.github.io")
                scm {
                    connection.set("scm:git:git@github.com/....")
                }
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("danilopianini")
                        name.set("Danilo Pianini")
                    }
                }
            }
        }
    }
}

val zipDistribution by tasks.registering(Zip::class) {
    archiveBaseName.set("central-distribution")
    from(localRepo)
    dependsOn(tasks.withType<PublishTask>().matching { "MyRepo" in it.name })
}
