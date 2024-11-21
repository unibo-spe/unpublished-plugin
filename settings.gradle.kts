plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "2.0.15"
}

rootProject.name = "spe-helloworld-plugin"

gitHooks {
    commitMsg { conventionalCommits() }
    createHooks(overwriteExisting = true)
}
