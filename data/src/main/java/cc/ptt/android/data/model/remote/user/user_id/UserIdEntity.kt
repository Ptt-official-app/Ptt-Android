package cc.ptt.android.data.model.remote.user.user_id

import com.google.gson.annotations.SerializedName

data class UserIdEntity(
    @SerializedName("user_id")
    val userId: String
)
