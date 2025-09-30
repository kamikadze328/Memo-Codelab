pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Memo Codelab"
include(
    ":app",
    ":domain",
    ":data",
    ":core-android",
    ":core-ui",
    ":background-location",
    ":feature-home",
    ":feature-memo-details",
    ":feature-memo-create",
    ":feature-choose-location"
)