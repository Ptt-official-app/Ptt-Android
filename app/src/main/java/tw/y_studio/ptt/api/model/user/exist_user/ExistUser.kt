package tw.y_studio.ptt.api.model.user.exist_user

import com.google.gson.annotations.SerializedName

data class ExistUser(
    @SerializedName("is_exists")
    val isExist: Boolean
)
