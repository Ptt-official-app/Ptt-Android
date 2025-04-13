// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.spotless)
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
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
