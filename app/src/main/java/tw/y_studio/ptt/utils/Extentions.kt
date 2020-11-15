package tw.y_studio.ptt.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun <T> LifecycleOwner.observe(liveData: LiveData<T?>, block: ((it: T?) -> Unit)) {
    liveData.observe(
        this,
        {
            block(it)
        }
    )
}

fun <T> LifecycleOwner.observeNotNull(liveData: LiveData<T>, block: ((it: T) -> Unit)) {
    liveData.observe(
        this,
        {
            block(it)
        }
    )
}
