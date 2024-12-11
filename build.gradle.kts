// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Project-level build.gradle.kts
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        // Adding the Butter Knife Gradle plugin for handling view binding
        classpath("com.android.tools.build:gradle:8.0.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0")
        classpath("com.jakewharton:butterknife-gradle-plugin:10.2.3")
    }
}


plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}