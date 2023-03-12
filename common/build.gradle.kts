plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "cc.ptt.android.common"
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = GlobalConfig.JDKVersion
        targetCompatibility = GlobalConfig.JDKVersion
    }
}

dependencies {
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

    coreLibraryDesugaring(Dependencies.desugar)

    testImplementation(Dependencies.junit)

    androidTestImplementation(Dependencies.AndroidX.Test.Ext.junit)
    androidTestImplementation(Dependencies.AndroidX.Test.Ext.espresso)
}