package cc.ptt.android.data.source.local

import cc.ptt.android.data.model.ui.user.UserInfo

interface LoginLocalDataSource {

    fun isLogin(): Boolean

    fun cleanUserInfo()

    fun setUserInfo(userInfo: UserInfo)

    fun getUserInfo(): UserInfo?
}
