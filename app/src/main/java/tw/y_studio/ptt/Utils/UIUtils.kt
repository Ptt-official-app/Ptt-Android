@file:JvmName("UIUtils")

package tw.y_studio.ptt.Utils

import android.content.Context
import android.content.Intent

/**
 * 使用Android分享選單分享
 *
 * @param context
 * @param subject 主題
 * @param body 內文
 * @param chooserTitle 選擇器標題
 */
fun shareTo(context: Context, subject: String, body: String, chooserTitle: String) {
    val sharingIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }
    context.startActivity(Intent.createChooser(sharingIntent, chooserTitle))
}
