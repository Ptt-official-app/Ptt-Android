import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("dagger.hilt.android.plugin")
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

fun getProductionApiHost(): String {
    return try {
        gradleLocalProperties(rootDir).getProperty("production_host")
    } catch (e: Exception) {
        getApiHost()
    }
}

fun getStagingApiHost(): String {
    return try {
        gradleLocalProperties(rootDir).getProperty("staging_host")
    } catch (e: Exception) {
        getApiHost()
    }
}

fun getApiHost(): String {
    return gradleLocalProperties(rootDir).getProperty("host")
}

android {
    compileSdk = GlobalConfig.ANDROID_BUILD_SDK_VERSION
    buildToolsVersion = GlobalConfig.ANDROID_BUILD_TOOLS_VERSION

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

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            isShrinkResources = true
            isZipAlignEnabled = true
            multiDexEnabled = true
        }
        getByName("debug") {
            isDefault = true
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            isShrinkResources = false
            isZipAlignEnabled = false
            multiDexEnabled = true
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = GlobalConfig.JDKVersion
        targetCompatibility = GlobalConfig.JDKVersion
    }

    buildFeatures {
        viewBinding = true
    }

    flavorDimensions.add("api_environment")
    productFlavors {
        create("production") {
            dimension = "api_environment"
            buildConfigField("String", "APIDomain", "\"${getProductionApiHost()}\"")
        }

        create("staging") {
            isDefault = true
            dimension = "api_environment"
            buildConfigField("String", "APIDomain", "\"${getStagingApiHost()}\"")
        }
    }

    sourceSets {
        val sharedTestDir = "src/sharedTest/java"
        getByName("test") {
            java.setSrcDirs(listOf(sharedTestDir))
        }
        getByName("androidTest") {
            java.setSrcDirs(listOf(sharedTestDir))
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    // AndroidX
    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.AndroidX.browser)
    implementation(Dependencies.AndroidX.constraintLayout)
    implementation(Dependencies.AndroidX.legacy)
    implementation(Dependencies.AndroidX.Lifecycle.compiler)
    implementation(Dependencies.AndroidX.Lifecycle.viewModelKTX)
    implementation(Dependencies.AndroidX.Lifecycle.runtime)
    implementation(Dependencies.AndroidX.localBroadcastManager)
    implementation(Dependencies.AndroidX.multidex)
    implementation(Dependencies.AndroidX.swipeRefreshLayout)
    implementation(Dependencies.AndroidX.Navigation.fragment)
    implementation(Dependencies.AndroidX.Navigation.ui)

    // Coil
    implementation(Dependencies.coil)

    // Google
    implementation(Dependencies.Google.material)
    implementation(Dependencies.Google.gson)

    // Jsoup
    implementation(Dependencies.jsoup)

    // Kotlin
    implementation(Dependencies.Kotlin.Stdlib.jdk8)
    implementation(Dependencies.Kotlin.Coroutines.core)
    implementation(Dependencies.Kotlin.Coroutines.android)
    // Square
    implementation(Dependencies.Square.okhttp)
    implementation(Dependencies.Square.log)
    implementation(Dependencies.Square.okio)
    implementation(Dependencies.Square.Retrofit.core)
    implementation(Dependencies.Square.Retrofit.gsonConverter)

    coreLibraryDesugaring(Dependencies.desugar)

    testImplementation(Dependencies.Google.truth)
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.MockK.core)
    testImplementation(Dependencies.AndroidX.arch)
    testImplementation(Dependencies.Kotlin.Coroutines.test)

    androidTestImplementation(Dependencies.AndroidX.Test.Ext.junit)
    androidTestImplementation(Dependencies.AndroidX.Test.Ext.espresso)
    androidTestImplementation(Dependencies.Google.truth)
    androidTestImplementation(Dependencies.MockK.android)
    androidTestImplementation(Dependencies.AndroidX.arch)
    androidTestImplementation(Dependencies.Kotlin.Coroutines.test)

    debugImplementation(Dependencies.Square.leakcanary)

    // solved AndroidX conflict: Duplicate class guava listenablefuture
    // see：https://stackoverflow.com/questions/60472354/duplicate-class-com-google-common-util-concurrent-listenablefuture-found-in-modu
    implementation("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")

    // Hilt
    implementation(Dependencies.Hilt.android)
    kapt(Dependencies.Hilt.compiler)
}

kapt {
    correctErrorTypes = true
}
