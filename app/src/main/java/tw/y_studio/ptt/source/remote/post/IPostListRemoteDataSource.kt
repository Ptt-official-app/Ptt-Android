package tw.y_studio.ptt.source.remote.post

import tw.y_studio.ptt.model.PartialPost

interface IPostListRemoteDataSource {

    fun getPostListData(boardName: String, page: Int): List<PartialPost>

    fun disposeAll()
}
