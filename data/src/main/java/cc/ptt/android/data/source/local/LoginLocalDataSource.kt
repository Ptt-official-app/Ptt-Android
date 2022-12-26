package cc.ptt.android.data.source.local

import cc.ptt.android.data.model.remote.user.login.LoginEntity

interface LoginLocalDataSource {

    fun isLogin(): Boolean

    fun cleanUserInfo()

    fun setUserInfo(userInfo: LoginEntity)

    fun getUserInfo(): LoginEntity?
}
