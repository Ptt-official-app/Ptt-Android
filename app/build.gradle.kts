plugins {
    id("com.android.application")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    kotlin("android")
}

fun String.runCommand(workingDir: File = file("./")): String {
    val parts = this.split("\\s".toRegex())
    val proc = ProcessBuilder(*parts.toTypedArray())
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()

    proc.waitFor(1, TimeUnit.MINUTES)
    return proc.inputStream.bufferedReader().readText().trim()
}

fun gitSha(): String {
    return "git rev-parse --short HEAD".runCommand()
}

android {
    namespace = GlobalConfig.applicationId
    compileSdk = GlobalConfig.ANDROID_BUILD_SDK_VERSION
    buildToolsVersion = GlobalConfig.ANDROID_BUILD_TOOLS_VERSION

    namespace = "cc.ptt.android"
    buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = GlobalConfig.applicationId
        minSdk = GlobalConfig.ANDROID_BUILD_MIN_SDK_VERSION
        targetSdk = GlobalConfig.ANDROID_BUILD_TARGET_SDK_VERSION
        versionCode = GlobalConfig.versionCode
        versionName = "${GlobalConfig.versionName}, ${gitSha()}"
        GlobalConfig.testInstrumentationRunner
        multiDexEnabled = true
        ndk {
            abiFilters.add("armeabi-v7a")
            abiFilters.add("arm64-v8a")
            abiFilters.add("x86")
            abiFilters.add("x86_64")
        }
    }

    signingConfigs {
        create("ptt_dev") {
            storeFile = file("debug.jks")
            storePassword = "PttNeverDie"
            keyAlias = "Ptt"
            keyPassword = "PttNeverDie"
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("ptt_dev")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isShrinkResources = true
            multiDexEnabled = true
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("ptt_dev")
            isDefault = true
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isShrinkResources = false
            multiDexEnabled = true
        }
    }

    compileOptions {
        sourceCompatibility = GlobalConfig.JDKVersion
        targetCompatibility = GlobalConfig.JDKVersion
    }

    buildFeatures {
        resValues = true
        viewBinding = true
    }

    flavorDimensions += "api_environment"
    productFlavors {
        create("production") {
            dimension = "api_environment"
        }

        create("staging") {
            isDefault = true
            dimension = "api_environment"
        }
    }

    packaging {
        resources {
            merges.add("META-INF/LICENSE.md")
            merges.add("META-INF/LICENSE-notice.md")
        }
    }
}

dependencies {
    api(project(":common"))
    api(project(":data"))
    api(project(":domain"))

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    // AndroidX
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.lifecycle.compiler)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.localbroadcastmanager)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // Coil
    implementation(libs.coil)

    // Google
    implementation(libs.google.material)
    implementation(libs.google.gson)

    // Jsoup
    implementation(libs.jsoup)

    // Kotlin
    //implementation(Dependencies.Kotlin.Stdlib.jdk8)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    // Square
    implementation(libs.square.okhttp)
    implementation(libs.square.log)
    implementation(libs.square.okio)
    implementation(libs.square.retrofit.core)
    implementation(libs.square.retrofit.gson.converter)

    // Koin Core features
    implementation(libs.koin.core)
    // Koin main features for Android
    implementation(libs.koin.android)
    implementation(libs.androidx.core)

    // Test
    testImplementation(libs.google.truth)
    testImplementation(libs.junit)
    testImplementation(libs.mockk.core)
    testImplementation(libs.androidx.arch)
    testImplementation(libs.coroutines.test)

    // Koin Test features
    testImplementation(libs.koin.test)
    // Koin for JUnit 4
    testImplementation(libs.koin.junit4)
    testImplementation(libs.androidx.test.core)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)

    androidTestImplementation(libs.google.truth)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.androidx.arch)
    androidTestImplementation(libs.coroutines.test)

    debugImplementation(libs.square.leakcanary)

}

