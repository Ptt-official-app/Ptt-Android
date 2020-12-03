package tw.y_studio.ptt.source.remote.post

import tw.y_studio.ptt.api.PostAPI
import tw.y_studio.ptt.model.PartialPost
import tw.y_studio.ptt.model.Post
import tw.y_studio.ptt.model.PostRank

class PostRemoteDataSourceImpl(
    private val postAPIHelper: PostAPI
) : IPostRemoteDataSource {
    override fun getPost(board: String, fileName: String): Post {
        return postAPIHelper.getPost(board, fileName)
    }

    override fun getPostRank(board: String, aid: String): PostRank {
        return postAPIHelper.getPostRank(board, aid)
    }

    override fun getPostList(broadName: String, page: Int): List<PartialPost> {
        return postAPIHelper.getPostList(broadName, page)
    }

    override fun disposeAll() {
        postAPIHelper.close()
    }
}
