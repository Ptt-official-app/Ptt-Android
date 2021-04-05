package tw.y_studio.ptt.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tw.y_studio.ptt.utils.event.Event
import tw.y_studio.ptt.utils.event.EventObserver

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

inline fun <T> LiveData<Event<T>>.observeEvent(
    owner: LifecycleOwner,
    crossinline eventObserver: (T?) -> Unit
) {
    this.observe(owner, EventObserver { eventObserver(it) })
}

inline fun <T> LiveData<Event<T>>.observeEventNotNull(
    owner: LifecycleOwner,
    crossinline eventObserver: (T) -> Unit
) {
    this.observe(owner, EventObserver { it?.run(eventObserver) })
}

inline fun <T> MutableLiveData<T>.forceRefresh() {
    this.value = this.value
}
