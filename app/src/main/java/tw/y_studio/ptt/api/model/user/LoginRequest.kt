package tw.y_studio.ptt.api.model.user

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("client_secret")
    val clientSecret: String,
    @SerializedName("username")
    val userName: String,
    @SerializedName("password")
    val password: String
)
