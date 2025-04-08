plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "cc.ptt.android.common"
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
}

dependencies {
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

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)
}