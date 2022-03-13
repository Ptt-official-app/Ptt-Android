package cc.ptt.android

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PttApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
    }
}
