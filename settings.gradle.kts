plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "2.0.15"
    id("com.gradle.develocity") version "3.18.2"
}

rootProject.name = "spe-helloworld-plugin"

gitHooks {
    preCommit {
        tasks("ktlintFormat", "ktlintCheck", "detekt")
    }
    commitMsg { conventionalCommits() }
    createHooks(overwriteExisting = true)
}

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
        termsOfUseAgree.set("yes")
    }
}
