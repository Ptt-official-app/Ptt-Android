package tw.y_studio.ptt.source.remote.rank

import tw.y_studio.ptt.api.PostRankAPI

/**
 * Created by Michael.Lien
 * on 2020/12/1
 */
interface IPostRankRemoteDataSource {
    fun setPostRank(boardTitle: String, aid: String, pttId: String, rank: PostRankAPI.PostRank)
}
