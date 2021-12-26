package cc.ptt.android.presentation.common.event

import androidx.lifecycle.Observer

/**
 * 這是一個再次封裝 [LiveData] 數據的封裝類，目的是為了讓 [LiveData] 的數據流可以過濾掉一些因為生命週期的關係而重複發送的資料，
 * 例如： LiveData 先顯示一個 Toast ，旋轉螢幕後 Activity/Fragment 會 destroy & create ，這時這個 Toast 的 LiveData 就會重新發射數據流並被 [Observer] 接收，把之前顯示的訊息再次顯示出來。
 */
open class Event<out T>(private val content: T) {
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun getContent(): T = content
}

/**
 * 簡化使用 [Event] 時的 [Observer] 操作行為
 */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}
