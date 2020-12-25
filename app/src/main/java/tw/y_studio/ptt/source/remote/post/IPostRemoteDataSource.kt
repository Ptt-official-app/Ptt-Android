package tw.y_studio.ptt.source.remote.post

import tw.y_studio.ptt.api.PostRankMark
import tw.y_studio.ptt.model.PartialPost
import tw.y_studio.ptt.model.Post
import tw.y_studio.ptt.model.PostRank

interface IPostRemoteDataSource {
    fun getPost(board: String, fileName: String): Post

    fun setPostRank(
        boardName: String,
        aid: String,
        pttId: String,
        rank: PostRankMark
    )

    fun getPostRank(board: String, aid: String): PostRank

    fun getPostList(broadName: String, page: Int): List<PartialPost>

    fun disposeAll()
}
