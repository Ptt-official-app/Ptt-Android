package cc.ptt.android.data.source.local

import cc.ptt.android.data.model.remote.user.login.LoginEntity
import cc.ptt.android.data.preference.UserInfoPreferences

class LoginLocalDataSourceImpl constructor(
    private val userInfoPreferences: UserInfoPreferences
) : LoginLocalDataSource {

    override fun isLogin(): Boolean {
        return getUserInfo()?.let {
            true
        } ?: false
    }

    override fun cleanUserInfo() {
        userInfoPreferences.setLogin(null)
    }

    override fun setUserInfo(loginEntity: LoginEntity) {
        userInfoPreferences.setLogin(loginEntity)
    }

    override fun getUserInfo(): LoginEntity? {
        return userInfoPreferences.getLogin()
    }
}
