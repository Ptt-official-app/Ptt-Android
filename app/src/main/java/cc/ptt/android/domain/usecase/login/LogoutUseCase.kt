package cc.ptt.android.domain.usecase.login

import cc.ptt.android.data.repository.login.LoginRepository
import cc.ptt.android.di.IODispatchers
import cc.ptt.android.domain.base.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    @IODispatchers dispatcher: CoroutineDispatcher
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
