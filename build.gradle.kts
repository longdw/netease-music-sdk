// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
//    alias(libs.plugins.android.application) apply false
//    alias(libs.plugins.kotlin.android) apply false

    id("com.android.application") version "7.4.2" apply false
    id("com.android.library") version "7.4.2" apply false
    id("com.google.devtools.ksp") version "1.7.10-1.0.6" apply false
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
}

tasks.register("clean") {
    doLast {
        delete(rootProject.layout.buildDirectory)
    }
}

//~~~~~~~~~~~~~~~start如果是smart maven发布start~~~~~~~~~~~~~~~
//apply(from = "smart-maven-publish.gradle.kts")
//~~~~~~~~~~~~~~~end如果是smart maven发布end~~~~~~~~~~~~~~~

//~~~~~~~~~~~~~~~start如果是maven central发布start~~~~~~~~~~~~~~~
apply(from = "maven-central-publish.gradle.kts")
//~~~~~~~~~~~~~~~end如果是maven central发布end~~~~~~~~~~~~~~~