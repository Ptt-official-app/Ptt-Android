package cc.ptt.android.data.model.remote

data class PostRank(
    val board: String,
    val aid: String,
    val goup: Int,
    val down: Int
) {
    fun getLike(): Int {
        return goup - down
    }
}
