package cc.ptt.android.common.extension

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlin.reflect.KProperty

interface ArgumentProvider<E, T> {
    operator fun provideDelegate(thisRef: E, prop: KProperty<*>): Lazy<T>
}

inline fun <F, reified T> argumentDelegate(
    crossinline provideArgument: (F) -> Bundle
): ArgumentProvider<F, T> = object : ArgumentProvider<F, T> {
    override fun provideDelegate(thisRef: F, prop: KProperty<*>) = lazy {
        val bundle = provideArgument(thisRef)
        bundle[prop.name] as T
    }
}

inline fun <reified T> Activity.bundleDelegate() = argumentDelegate<Activity, T> {
    it.intent?.extras ?: throw RuntimeException("No arguments passed")
}

inline fun <reified T> Fragment.bundleDelegate() = argumentDelegate<Fragment, T> {
    it.arguments ?: throw RuntimeException("No arguments passed")
}
