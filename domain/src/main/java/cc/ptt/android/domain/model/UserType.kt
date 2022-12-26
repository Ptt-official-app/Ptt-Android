package cc.ptt.android.domain.model

import cc.ptt.android.domain.model.ui.user.UserInfo

sealed class UserType {
    object Guest : UserType()
    data class Login(val userInfo: UserInfo) : UserType()
}
