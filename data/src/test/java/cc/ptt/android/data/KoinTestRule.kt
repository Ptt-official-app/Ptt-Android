package cc.ptt.android.data

import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.module.Module

class KoinTestRule(
    private val modules: List<Module>
) : TestWatcher() {

    override fun starting(description: Description) {
        startKoin {
            modules(modules)
        }
    }

    override fun finished(description: Description) {
        stopKoin()
    }
}
