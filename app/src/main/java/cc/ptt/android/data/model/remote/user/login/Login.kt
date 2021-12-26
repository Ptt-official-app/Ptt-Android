package cc.ptt.android.data.model.remote.user.login
import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("user_id")
    val userId: String
)
