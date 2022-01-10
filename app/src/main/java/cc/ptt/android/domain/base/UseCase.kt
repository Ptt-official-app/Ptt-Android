package cc.ptt.android.domain.base

import kotlinx.coroutines.*

abstract class UseCase<in P, R>(private val dispatcher: CoroutineDispatcher) {
    private val TAG = this.javaClass.simpleName

    suspend operator fun invoke(parameters: P): Result<R> {
        return withContext(dispatcher) {
            return@withContext executeNow(parameters)
        }
    }

    operator fun invoke(
        scope: CoroutineScope = GlobalScope,
        parameters: P,
        onResult: (Result<R>) -> Unit = {}
    ) {
        scope.launch {
            onResult(
                withContext(dispatcher) {
                    return@withContext executeNow(parameters)
                }
            )
        }
    }

    private suspend fun executeNow(parameters: P): Result<R> {
        return try {
            execute(parameters)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    protected abstract suspend fun execute(parameters: P): Result<R>
}

suspend operator fun <R> UseCase<Unit, R>.invoke(): Result<R> = this(Unit)
