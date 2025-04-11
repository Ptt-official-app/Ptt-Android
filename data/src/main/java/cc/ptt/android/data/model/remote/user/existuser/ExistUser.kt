package cc.ptt.android.data.model.remote.user.existuser

import com.google.gson.annotations.SerializedName

data class ExistUser(
    @SerializedName("is_exists")
    val isExist: Boolean,
)
