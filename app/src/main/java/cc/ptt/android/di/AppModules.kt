package cc.ptt.android.di

import android.content.Context
import cc.ptt.android.common.apihelper.ApiHelper
import cc.ptt.android.common.apihelper.ApiHelperImpl
import cc.ptt.android.common.security.AESKeyStoreHelper
import cc.ptt.android.common.security.AESKeyStoreHelperImpl
import cc.ptt.android.data.common.PreferenceConstants
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModules = module {
    single(named(MainPreferences)) { androidContext().getSharedPreferences(PreferenceConstants.prefName, Context.MODE_PRIVATE) }
    factory <AESKeyStoreHelper> { AESKeyStoreHelperImpl() }
    factory <ApiHelper> { ApiHelperImpl(get(named(MainPreferences))) }
}

const val MainPreferences = "MainPreferences"
