package cc.ptt.android.domain.usecase.login

import cc.ptt.android.common.apihelper.ApiHelper
import cc.ptt.android.data.model.ui.user.UserInfo
import cc.ptt.android.data.repository.login.LoginRepository
import cc.ptt.android.domain.base.UseCaseBase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginUseCase constructor(
    private val loginRepository: LoginRepository,
    private val apiHelper: ApiHelper
) : UseCaseBase() {

    fun login(id: String, password: String): Flow<UserInfo> {
        return loginRepository.login(apiHelper.getClientId(), apiHelper.getClientSecret(), id, password).map {
            loginRepository.getUserInfo() ?: throw Exception("Not login yet")
        }
    }

    fun logout(): Flow<Unit> {
        return loginRepository.logout()
    }
}
