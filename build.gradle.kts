// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://cdn.reproio.com/android")
    }
    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
    }
}

plugins {
    id("com.diffplug.spotless") version Versions.spotless
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
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
            targetExclude("${layout.buildDirectory}/**/*.kt")
            targetExclude("bin/**/*.kt")
            trimTrailingWhitespace()
            endWithNewline()
            // 允許 import 路徑使用萬用字元
            ktlint("1.5.0").userData(mapOf("disabled_rules" to "no-wildcard-imports"))
        }
        java {
            target ("src/*/java/**/*.java")
            googleJavaFormat("1.21.0").aosp()
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
