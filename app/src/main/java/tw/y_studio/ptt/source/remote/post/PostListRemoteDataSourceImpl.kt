package tw.y_studio.ptt.source.remote.post

import tw.y_studio.ptt.api.PostListAPI
import tw.y_studio.ptt.model.PartialPost

class PostListRemoteDataSourceImpl(
    private val postListAPI: PostListAPI
) : IPostListRemoteDataSource {
    override fun getPostListData(boardName: String, page: Int): List<PartialPost> {
        return postListAPI.loadBroadListData(boardName, page)
    }

    override fun disposeAll() {
        postListAPI.close()
    }
}
