package cc.ptt.android.data.model.remote.user.user_id

import com.google.gson.annotations.SerializedName

data class UserIdRequest(
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("client_secret")
    val clientSecret: String
)
