package tw.y_studio.ptt.model

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
