@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.multiJvmTesting)
    alias(libs.plugins.gitSemVer)
    alias(libs.plugins.gradlePluginPublish)
    alias(libs.plugins.kotlin.qa)
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
