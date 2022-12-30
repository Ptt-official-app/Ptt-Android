package cc.ptt.android.data.preference

import cc.ptt.android.data.model.remote.user.login.LoginEntity

interface UserInfoPreferences {
    fun setLogin(loginEntity: LoginEntity?)
    fun getLogin(): LoginEntity?
}
