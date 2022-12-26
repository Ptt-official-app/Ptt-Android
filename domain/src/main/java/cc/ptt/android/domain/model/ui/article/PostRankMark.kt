package cc.ptt.android.domain.model.ui.article

enum class PostRankMark(val value: Int) {
    Like(1), Dislike(-1), None(0)
}
