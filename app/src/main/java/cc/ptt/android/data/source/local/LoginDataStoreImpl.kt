package cc.ptt.android.data.source.local

import android.content.SharedPreferences
import cc.ptt.android.common.security.AESKeyStoreHelper
import cc.ptt.android.data.common.PreferenceConstants
import cc.ptt.android.data.common.StringUtils
import cc.ptt.android.data.model.ui.user.UserInfo
import javax.inject.Inject

class LoginDataStoreImpl @Inject constructor(
    private val preference: SharedPreferences
) : LoginDataStore {

    private val keyStoreHelper = AESKeyStoreHelper()

    override fun isLogin(): Boolean {
        return getUserInfo()?.let {
            true
        } ?: false
    }

    override fun cleanUserInfo() {
        val editor = preference.edit()
        editor.remove(StringUtils.notNullImageString(PreferenceConstants.id))
        editor.remove(PreferenceConstants.accessToken)
        editor.remove(PreferenceConstants.tokenType)
        editor.apply()
        editor.commit()
    }

    override fun setUserInfo(userInfo: UserInfo) {
        val editor = preference.edit()
        editor.putString(PreferenceConstants.id, userInfo.id)
        editor.putString(PreferenceConstants.accessToken, keyStoreHelper.encrypt(userInfo.accessToken))
        editor.putString(PreferenceConstants.tokenType, userInfo.tokenType)
        editor.apply()
        editor.commit()
    }

    override fun getUserInfo(): UserInfo? {
        val id = preference.getString(PreferenceConstants.id, "")
        val accessToken = preference.getString(PreferenceConstants.accessToken, "")
        val tokenType = preference.getString(PreferenceConstants.tokenType, "")
        return if (id.isNullOrEmpty() || accessToken.isNullOrEmpty() || tokenType.isNullOrEmpty()) {
            null
        } else {
            UserInfo(id, keyStoreHelper.decrypt(accessToken), tokenType)
        }
    }
}
