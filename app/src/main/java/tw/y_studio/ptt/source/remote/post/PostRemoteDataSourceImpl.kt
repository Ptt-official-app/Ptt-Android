package tw.y_studio.ptt.source.remote.post

import tw.y_studio.ptt.api.PostAPI
import tw.y_studio.ptt.model.Post

class PostRemoteDataSourceImpl(
    private val postAPIHelper: PostAPI
) : IPostRemoteDataSource {
    override fun getPostData(board: String, fileName: String): Post {
        return postAPIHelper.getPost(board, fileName)
    }

    override fun disposeAll() {
        postAPIHelper.close()
    }
}
