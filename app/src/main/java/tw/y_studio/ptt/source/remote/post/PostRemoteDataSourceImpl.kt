package tw.y_studio.ptt.source.remote.post

import tw.y_studio.ptt.api.PostAPI
import tw.y_studio.ptt.api.PostRankMark
import tw.y_studio.ptt.api.model.PartialPost
import tw.y_studio.ptt.api.model.Post
import tw.y_studio.ptt.api.model.PostRank

class PostRemoteDataSourceImpl(
    private val postAPI: PostAPI
) : IPostRemoteDataSource {
    override fun getPost(board: String, fileName: String): Post {
        return postAPI.getPost(board, fileName)
    }

    override fun setPostRank(
        boardName: String,
        aid: String,
        pttId: String,
        rank: PostRankMark
    ) {
        postAPI.setPostRank(boardName, aid, pttId, rank)
    }

    override fun getPostRank(board: String, aid: String): PostRank {
        return postAPI.getPostRank(board, aid)
    }

    override fun getPostList(broadName: String, page: Int): List<PartialPost> {
        return postAPI.getPostList(broadName, page)
    }

    override fun disposeAll() {
        postAPI.close()
    }
}
