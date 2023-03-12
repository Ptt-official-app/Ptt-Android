import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    kotlin("android")
}

fun getProductionHost(): String {
    return try {
        checkStringType(gradleLocalProperties(rootDir).getProperty("PRODUCTION_HOST"))
    } catch (e : Exception) {
        getHost()
    }
}

fun getStagingHost(): String {
    return try {
        checkStringType(gradleLocalProperties(rootDir).getProperty("STAGING_HOST"))
    } catch (e : Exception) {
        getHost()
    }
}

fun getHost(): String {
    return try {
        checkStringType(gradleLocalProperties(rootDir).getProperty("HOST"))
    } catch (e : Exception) {
        "\"\""
    }
}

fun getTestAccount(): String {
    return try {
        checkStringType(gradleLocalProperties(rootDir).getProperty("ACCOUNT"))
    } catch (e : Exception) {
        "\"\""
    }
}

fun getTestPassword(): String {
    return try {
        checkStringType(gradleLocalProperties(rootDir).getProperty("PASSWORD"))
    } catch (e : Exception) {
        "\"\""
    }
}

fun checkStringType(text: String): String {
    return "\"${text.replace("\"","")}\""
}

android {
    namespace = "cc.ptt.android.data"
    compileSdk = GlobalConfig.ANDROID_BUILD_SDK_VERSION

    defaultConfig {
        minSdk = GlobalConfig.ANDROID_BUILD_MIN_SDK_VERSION
        targetSdk = GlobalConfig.ANDROID_BUILD_TARGET_SDK_VERSION

        testInstrumentationRunner = GlobalConfig.testInstrumentationRunner
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = GlobalConfig.JDKVersion
        targetCompatibility = GlobalConfig.JDKVersion
    }

    flavorDimensions += "api_environment"
    productFlavors {
        create("production") {
            dimension = "api_environment"
            buildConfigField("String", GlobalConfig.BUILD_CONFIG_KEY_FOR_API_HOST, getProductionHost())
            buildConfigField("String", GlobalConfig.BUILD_CONFIG_KEY_FOR_TEST_ACCOUNT, "\"\"")
            buildConfigField("String", GlobalConfig.BUILD_CONFIG_KEY_FOR_TEST_PASSWORD, "\"\"")
        }

        create("staging") {
            isDefault = true
            dimension = "api_environment"
            buildConfigField("String", GlobalConfig.BUILD_CONFIG_KEY_FOR_API_HOST, getStagingHost())
            buildConfigField("String", GlobalConfig.BUILD_CONFIG_KEY_FOR_TEST_ACCOUNT, getTestAccount())
            buildConfigField("String", GlobalConfig.BUILD_CONFIG_KEY_FOR_TEST_PASSWORD, getTestPassword())
        }
    }

    testOptions {
        unitTests.all {
            it.ignoreFailures = true
        }
    }
}

dependencies {
    api(project(":common"))

    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.Google.material)
    // Koin Core features
    implementation(Dependencies.Koin.Core.core)
    // Koin main features for Android
    implementation(Dependencies.Koin.Android.android)
    // Kotlin
    implementation(Dependencies.Kotlin.Coroutines.core)
    implementation(Dependencies.Kotlin.Coroutines.android)
    // Square
    implementation(Dependencies.Square.okhttp)
    implementation(Dependencies.Square.log)
    implementation(Dependencies.Square.okio)
    implementation(Dependencies.Square.Retrofit.core)
    implementation(Dependencies.Square.Retrofit.gsonConverter)
    implementation(Dependencies.AndroidX.coreKtx)

    testImplementation(Dependencies.Google.truth)
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.MockK.core)
    testImplementation(Dependencies.AndroidX.arch)
    testImplementation(Dependencies.Kotlin.Coroutines.test)

    // Koin Test features
    testImplementation(Dependencies.Koin.Core.test)
    // Koin for JUnit 4
    testImplementation(Dependencies.Koin.Core.junit4)
    testImplementation(Dependencies.AndroidX.Test.Core.core)

    testImplementation(Dependencies.junit)

    androidTestImplementation(Dependencies.AndroidX.Test.Ext.junit)
    androidTestImplementation(Dependencies.AndroidX.Test.Ext.espresso)
}