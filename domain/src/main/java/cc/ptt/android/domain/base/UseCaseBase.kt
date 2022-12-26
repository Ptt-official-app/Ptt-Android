package cc.ptt.android.domain.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.koin.core.component.KoinComponent

open class UseCaseBase : KoinComponent, CoroutineScope by MainScope()
