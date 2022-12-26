package cc.ptt.android.data.model.remote.user.exist_user

import com.google.gson.annotations.SerializedName

data class ExistUser(
    @SerializedName("is_exists")
    val isExist: Boolean
)
