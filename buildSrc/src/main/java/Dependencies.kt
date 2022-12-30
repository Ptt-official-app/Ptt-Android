object Dependencies {

    object ProjectDependencies  {
        const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradle}"

        object Kotlin {
            const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        }

        const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    }

    object Kotlin {
        object Stdlib {
            const val jdk8 = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
        }

        object Coroutines {
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
        }
    }

    object AndroidX {
        const val arch = "androidx.arch.core:core-testing:${Versions.androidXArch}"
        const val appcompat = "androidx.appcompat:appcompat:${Versions.androidXAppCompat}"
        const val browser = "androidx.browser:browser:${Versions.androidXBrowser}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.androidXConstraintLayout}"
        const val legacy = "androidx.legacy:legacy-support-v4:${Versions.androidXLegacy}"

        object Lifecycle {
            const val extensions = "androidx.lifecycle:lifecycle-extensions:${Versions.androidXLifecycle}"
            const val compiler = "androidx.lifecycle:lifecycle-compiler:${Versions.androidXLifecycle}"
            const val viewModelKTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.androidXLifecycle}"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.androidXLifecycle}"
        }

        object Navigation {
            const val fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.androidXNavigation}"
            const val ui = "androidx.navigation:navigation-ui-ktx:${Versions.androidXNavigation}"
        }

        const val localBroadcastManager = "androidx.localbroadcastmanager:localbroadcastmanager:${Versions.androidXLocalBroadcastManager}"
        const val multidex = "androidx.multidex:multidex:${Versions.androidXMultidex}"
        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.androidXSwipeRefreshLayout}"
        const val coreKtx = "androidx.core:core-ktx:+"

        object Test {
            object Core {
                const val core = "androidx.test:core-ktx:${Versions.androidXTextCore}"
            }

            object Ext {
                const val junit = "androidx.test.ext:junit:${Versions.androidXTextJunit}"
                const val espresso = "androidx.test.espresso:espresso-core:${Versions.androidXTextEspresso}"
            }
        }

    }

    object Google {
        const val material = "com.google.android.material:material:${Versions.material}"
        const val truth = "com.google.truth:truth:${Versions.truth}"
        const val gson = "com.google.code.gson:gson:${Versions.gson}"
    }

    const val jsoup = "org.jsoup:jsoup:${Versions.jsoup}"
    const val junit = "junit:junit:${Versions.junit}"

    object MockK {
        const val core = "io.mockk:mockk:${Versions.mockK}"
        const val android = "io.mockk:mockk-android:${Versions.mockK}"
    }

    object Square {
        const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
        const val log = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
        const val okio = "com.squareup.okio:okio:${Versions.okio}"
        const val leakcanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakcanary}"

        object Retrofit {
            const val core = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
            const val gsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
        }
    }

    const val desugar = "com.android.tools:desugar_jdk_libs:${Versions.desugar}"
    const val coil = "io.coil-kt:coil:${Versions.coil}"

    object Hilt {
        const val android = "com.google.dagger:hilt-android:${Versions.hilt}"
        const val compiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    }

    object Koin {
        object Core {
            const val core = "io.insert-koin:koin-core:${Versions.koinCore}"
            const val test = "io.insert-koin:koin-test:${Versions.koinCore}"
            const val junit4 = "io.insert-koin:koin-test-junit4:${Versions.koinCore}"
        }
        object Android {
            const val android = "io.insert-koin:koin-android:${Versions.koinAndroid}"
        }
    }
}