pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { setUrl ("https://jitpack.io") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenCentral() // For ImagePicker library, this line is enough. Although, it has been published on jitpack as well
        maven { setUrl ("https://jitpack.io") }
    }
}

rootProject.name = "clientComic"
include(":app")
 