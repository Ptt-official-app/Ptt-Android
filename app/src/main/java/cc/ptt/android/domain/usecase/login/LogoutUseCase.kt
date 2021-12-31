package cc.ptt.android.domain.usecase.login

import cc.ptt.android.data.repository.login.LoginRepository
import cc.ptt.android.domain.base.UseCase
import kotlinx.coroutines.CoroutineDispatcher

class LogoutUseCase constructor(
    private val loginRepository: LoginRepository,
    dispatcher: CoroutineDispatcher
) : UseCase<Unit, Unit>(dispatcher) {

    override suspend fun execute(parameters: Unit): Result<Unit> {
        return try {
            loginRepository.logout()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
