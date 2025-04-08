import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    kotlin("android")
}

fun getProductionHost(): String {
    return try {
        checkStringType(gradleLocalProperties(rootDir, providers).getProperty("PRODUCTION_HOST"))
    } catch (e : Exception) {
        getHost()
    }
}

fun getStagingHost(): String {
    return try {
        checkStringType(gradleLocalProperties(rootDir, providers).getProperty("STAGING_HOST"))
    } catch (e : Exception) {
        getHost()
    }
}

fun getHost(): String {
    return try {
        checkStringType(gradleLocalProperties(rootDir, providers).getProperty("HOST"))
    } catch (e : Exception) {
        "\"\""
    }
}

fun getTestAccount(): String {
    return try {
        checkStringType(gradleLocalProperties(rootDir, providers).getProperty("ACCOUNT"))
    } catch (e : Exception) {
        "\"\""
    }
}

fun getTestPassword(): String {
    return try {
        checkStringType(gradleLocalProperties(rootDir, providers).getProperty("PASSWORD"))
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
    buildFeatures.buildConfig = true

    defaultConfig {
        minSdk = GlobalConfig.ANDROID_BUILD_MIN_SDK_VERSION

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

    buildFeatures {
        buildConfig = true
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

    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
    // Koin Core features
    implementation(libs.koin.core)
    // Koin main features for Android
    implementation(libs.koin.android)
    // Kotlin
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    // Square
    implementation(libs.square.okhttp)
    implementation(libs.square.log)
    implementation(libs.square.okio)
    implementation(libs.square.retrofit.core)
    implementation(libs.square.retrofit.gson.converter)
    implementation(libs.androidx.core)

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

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)
}