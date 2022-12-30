@file:JvmName("WebUtils")

package cc.ptt.android.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import cc.ptt.android.R

fun turnOnUrl(context: Context, url: String?) {
    val sharingIntent = Intent(Intent.ACTION_SEND)
    sharingIntent.type = "text/plain"
    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, url)
    sharingIntent.putExtra(Intent.EXTRA_TEXT, url)
    val builder = CustomTabsIntent.Builder()
    builder.addDefaultShareMenuItem()
    builder.setToolbarColor(context.resources.getColor(R.color.black))
    builder.setShowTitle(true)
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(context, Uri.parse(url))
}

fun isConnected(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

    return (activeNetwork?.isConnectedOrConnecting == true)
}
