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

rootProject.name = "TestNeteaseSdk"
include(":app")
include(":netease-music-base")
include(":iotsdk-extension")
include(":iotsdk-httpdns")
include(":netease-music-data")
include(":netease-music-player")
include(":netease-music-business")
include(":decoder-flac-aar")
 