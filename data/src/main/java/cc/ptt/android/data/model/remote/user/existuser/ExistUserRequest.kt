package cc.ptt.android.data.model.remote.user.existuser

import com.google.gson.annotations.SerializedName

data class ExistUserRequest(
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("client_secret")
    val clientSecret: String,
    @SerializedName("username")
    val userName: String,
)
