import org.gradle.api.JavaVersion

object Versions {
    const val majorVersion = 0
    const val minorVersion = 18
    const val patchVersion = 4
    const val spotless = "6.22.0"
}

object GlobalConfig {
    const val ANDROID_BUILD_SDK_VERSION = 35
    const val ANDROID_BUILD_MIN_SDK_VERSION = 23
    const val ANDROID_BUILD_TARGET_SDK_VERSION = 35
    const val ANDROID_BUILD_TOOLS_VERSION = "35.0.0"

    const val applicationId: String = "cc.ptt.android"
    const val versionCode: Int = Versions.majorVersion * 1000000 + Versions.minorVersion * 10000 + Versions.patchVersion * 100
    const val versionName = "${Versions.majorVersion}.${Versions.minorVersion}.${Versions.patchVersion}"

    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    val JDKVersion = JavaVersion.VERSION_21

    const val BUILD_CONFIG_KEY_FOR_API_HOST = "API_HOST"
    const val BUILD_CONFIG_KEY_FOR_TEST_ACCOUNT = "TEST_ACCOUNT"
    const val BUILD_CONFIG_KEY_FOR_TEST_PASSWORD = "TEST_PASSWORD"
}