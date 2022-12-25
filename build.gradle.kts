// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://cdn.reproio.com/android")
    }
    dependencies {
        classpath(Dependencies.ProjectDependencies.androidGradlePlugin)
        classpath(Dependencies.ProjectDependencies.Kotlin.gradlePlugin)
        classpath(Dependencies.ProjectDependencies.hiltGradlePlugin)
    }
}

plugins {
    id("com.diffplug.spotless") version Versions.spotless
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://cdn.reproio.com/android")
    }
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target ("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")
            trimTrailingWhitespace()
            endWithNewline()
            // 允許 import 路徑使用萬用字元
            ktlint("0.39.0").userData(mapOf("disabled_rules" to "no-wildcard-imports"))
        }
        java {
            target ("src/*/java/**/*.java")
            googleJavaFormat("1.7").aosp()
            // 移除沒用到的 Import
            removeUnusedImports()
            // 刪除多餘的空白
            trimTrailingWhitespace()
            importOrder("android", "androidx", "com", "junit", "net", "org", "java", "javax")
        }
    }

    task("format") {
        dependsOn("spotlessApply")
        group = "Verification"
    }
    task("formatCheck") {
        dependsOn("spotlessCheck")
        group = "Verification"
    }
}
