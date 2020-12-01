package tw.y_studio.ptt.source.remote.rank

import tw.y_studio.ptt.api.PostRankAPI

class PostRankRemoteDataSourceImpl(
    private val postRankAPI: PostRankAPI
) : IPostRankRemoteDataSource {
    override fun setPostRank(
        boardTitle: String,
        aid: String,
        pttId: String,
        rank: PostRankAPI.PostRank
    ) {
        postRankAPI.setPostRank(boardTitle, aid, pttId, rank)
    }
}
