package tw.y_studio.ptt.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Michael.Lien
 * on 2020/12/1
 */
data class PostRankResponse(
    val rank: String,

    @SerializedName("pttid")
    val pttId: String,

    @SerializedName("no")
    val number: String,

    val board: String,

    val aid: String
)
