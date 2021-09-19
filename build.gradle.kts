// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    extra["kotlin_version"] = "1.5.30"
    extra["navigation_version"] = "2.3.5"
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.0-alpha12")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${project.extra["kotlin_version"]}")
        classpath("de.mannodermaus.gradle.plugins:android-junit5:1.7.1.1")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${project.extra["navigation_version"]}")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.0.1" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
