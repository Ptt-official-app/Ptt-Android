package cc.ptt.android.domain.usecase.login

import cc.ptt.android.data.model.ui.user.UserInfo
import cc.ptt.android.data.repository.login.LoginRepository
import cc.ptt.android.di.IODispatchers
import cc.ptt.android.domain.base.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    @IODispatchers dispatcher: CoroutineDispatcher
) : UseCase<LoginUseCase.Params, LoginUseCase.Results>(dispatcher) {
    data class Params(
        val id: String,
        val password: String
    )

    data class Results(
        val data: UserInfo,
    )

    override suspend fun execute(parameters: Params): Result<Results> {
        return try {
            loginRepository.login("test_client_id", "test_client_secret", parameters.id, parameters.password)
            loginRepository.getUserInfo()?.let {
                Result.success(Results(it))
            } ?: throw Exception("Not login yet")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
