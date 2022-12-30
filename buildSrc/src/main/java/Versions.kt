import org.gradle.api.JavaVersion

object Versions {
    const val majorVersion = 0
    const val minorVersion = 17
    const val patchVersion = 1

    const val androidGradle = "7.2.2"
    const val kotlin = "1.7.22"
    const val androidXAppCompat = "1.5.1"
    const val androidXArch = "2.1.0"
    const val androidXBrowser = "1.4.0"
    const val androidXConstraintLayout = "2.0.4"
    const val androidXLegacy = "1.0.0"
    const val androidXLifecycle = "2.5.1"
    const val androidXNavigation = "2.5.3"
    const val androidXLocalBroadcastManager = "1.0.0"
    const val androidXMultidex = "2.0.1"
    const val androidXSwipeRefreshLayout = "1.1.0"
    const val androidXTextCore = "1.5.0"
    const val androidXTextJunit = "1.1.3"
    const val androidXTextEspresso = "3.3.0"
    const val jsoup = "1.13.1"
    const val junit = "4.13"
    const val material = "1.2.1"
    const val mockK = "1.10.2"
    const val okhttp = "4.9.0"
    const val okio = "2.10.0"
    const val retrofit = "2.9.0"
    const val truth = "1.1"
    const val leakcanary = "2.10"
    const val gson = "2.8.6"
    const val coroutines = "1.6.1"
    const val desugar = "1.1.5"
    const val coil = "2.2.2"
    const val hilt = "2.44"
    const val koinCore = "3.3.0"
    const val koinAndroid = "3.3.1"
    const val spotless = "6.12.0"
}

object GlobalConfig {
    const val ANDROID_BUILD_SDK_VERSION = 33
    const val ANDROID_BUILD_MIN_SDK_VERSION = 23
    const val ANDROID_BUILD_TARGET_SDK_VERSION = 33
    const val ANDROID_BUILD_TOOLS_VERSION = "33.0.0"

    const val applicationId: String = "cc.ptt.android"
    const val versionCode: Int = Versions.majorVersion * 1000000 + Versions.minorVersion * 10000 + Versions.patchVersion * 100
    const val versionName = "${Versions.majorVersion}.${Versions.minorVersion}.${Versions.patchVersion}"

    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    val JDKVersion = JavaVersion.VERSION_1_8

    const val BUILD_CONFIG_KEY_FOR_API_HOST = "API_HOST"
    const val BUILD_CONFIG_KEY_FOR_TEST_ACCOUNT = "TEST_ACCOUNT"
    const val BUILD_CONFIG_KEY_FOR_TEST_PASSWORD = "TEST_PASSWORD"
}