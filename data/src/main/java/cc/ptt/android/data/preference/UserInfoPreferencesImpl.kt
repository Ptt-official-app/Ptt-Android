package cc.ptt.android.data.preference

import android.content.Context
import android.content.SharedPreferences
import cc.ptt.android.common.security.AESKeyStoreHelper
import cc.ptt.android.data.model.remote.user.login.LoginEntity

class UserInfoPreferencesImpl constructor(
    private val context: Context,
    private val keyStoreHelper: AESKeyStoreHelper
) : UserInfoPreferences {

    private val preference: SharedPreferences by lazy { context.getSharedPreferences(KEY_FOR_PREFERENCE, Context.MODE_PRIVATE) }

    override fun setLogin(loginEntity: LoginEntity?) {
        loginEntity?.let {
            val editor = preference.edit()
            editor.putString(KEY_FOR_ID, loginEntity.userId)
            editor.putString(KEY_FOR_ACCESS_TOKEN, keyStoreHelper.encrypt(loginEntity.accessToken))
            editor.putString(KEY_FOR_TOKEN_TYPE, loginEntity.tokenType)
            editor.apply()
            editor.commit()
        } ?: run {
            val editor = preference.edit()
            editor.remove(KEY_FOR_ID)
            editor.remove(KEY_FOR_ACCESS_TOKEN)
            editor.remove(KEY_FOR_TOKEN_TYPE)
            editor.apply()
            editor.commit()
        }
    }

    override fun getLogin(): LoginEntity? {
        val id = preference.getString(KEY_FOR_ID, "")
        val accessToken = preference.getString(KEY_FOR_ACCESS_TOKEN, "")
        val tokenType = preference.getString(KEY_FOR_TOKEN_TYPE, "")
        return if (id.isNullOrEmpty() || accessToken.isNullOrEmpty() || tokenType.isNullOrEmpty()) {
            null
        } else {
            LoginEntity(userId = id, accessToken = keyStoreHelper.decrypt(accessToken), tokenType = tokenType)
        }
    }

    companion object {
        const val KEY_FOR_PREFERENCE = "user_info_preferences"
        const val KEY_FOR_ID = "key_for_id"
        const val KEY_FOR_ACCESS_TOKEN = "key_for_access_token"
        const val KEY_FOR_TOKEN_TYPE = "key_for_token_type"
    }
}
