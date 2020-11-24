package tw.y_studio.ptt.source.remote.post

import tw.y_studio.ptt.model.Post

interface IPostRemoteDataSource {
    fun getPostData(board: String, fileName: String): Post
    fun disposeAll()
}
