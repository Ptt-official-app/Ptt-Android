package cc.ptt.android.data.model.remote

import cc.ptt.android.common.network.api.ApiException
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class ServerMessage(
    @SerializedName("Msg")
    val msg: String
)

val ApiException.serverMsg: ServerMessage get() = try {
    Gson().fromJson(this.message, ServerMessage::class.java)
} catch (_: Throwable) {
    ServerMessage(this.message.orEmpty())
}
