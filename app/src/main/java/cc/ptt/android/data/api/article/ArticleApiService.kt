package cc.ptt.android.data.api.article

import cc.ptt.android.data.model.remote.article.ArticleCommentsList
import cc.ptt.android.data.model.remote.article.ArticleDetail
import cc.ptt.android.data.model.remote.article.ArticleRank
import cc.ptt.android.data.model.remote.article.hotarticle.HotArticleList
import okhttp3.RequestBody
import retrofit2.http.*

interface ArticleApiService {
    @GET("api/board/{bid}/article/{aid}")
    suspend fun getArticleDetail(
        @Path("bid") boardId: String,
        @Path("aid") articleId: String
    ): ArticleDetail

    @GET("api/board/{bid}/article/{aid}/comments")
    suspend fun getArticleComments(
        @Path("bid") boardId: String,
        @Path("aid") articleId: String,
        @Query("desc") desc: Boolean
    ): ArticleCommentsList

    @Headers("Content-Type: text/plain; charset=utf-8")
    @POST("api/board/{bid}/article/{aid}/rank")
    suspend fun postArticleRank(
        @Path("bid") boardId: String,
        @Path("aid") articleId: String,
        @Body body: RequestBody
    ): ArticleRank

    @Headers("Content-Type: text/plain; charset=utf-8")
    @GET("api/articles/popular")
    suspend fun getPopularArticles(
        @Query("start_idx") startIndex: String,
        @Query("limit") limit: Int,
        @Query("desc") desc: Boolean,
    ): HotArticleList
}
