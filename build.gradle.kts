@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.multiJvmTesting)
    alias(libs.plugins.gitSemVer)
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
        website.set("unibo-spe.github.io")
        vcsUrl.set("scm:git:$website.git")
        create("") {
            id = "$group.${project.name}"
            displayName = "SPE test plugin"
            description = project.description
            implementationClass = "$group.template.HelloGradle"
            tags = listOf("unibo", "spe", "software process engineering", "teaching", "educational")
        }
    }
}
