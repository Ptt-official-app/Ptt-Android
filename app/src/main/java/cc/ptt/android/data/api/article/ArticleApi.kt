package cc.ptt.android.data.api.article

import cc.ptt.android.data.model.remote.PostRank
import cc.ptt.android.data.model.remote.article.ArticleComment
import cc.ptt.android.data.model.remote.article.ArticleCommentsList
import cc.ptt.android.data.model.remote.article.ArticleDetail
import cc.ptt.android.data.model.remote.article.ArticleRank
import cc.ptt.android.data.model.remote.article.hotarticle.HotArticleList
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import retrofit2.http.*

interface ArticleApi {

    @GET("api/board/{bid}/article/{aid}")
    fun getArticleDetail(
        @Path("bid") boardId: String,
        @Path("aid") articleId: String
    ): Flow<ArticleDetail>

    @GET("api/board/{bid}/article/{aid}/comments")
    fun getArticleComments(
        @Path("bid") boardId: String,
        @Path("aid") articleId: String,
        @Query("desc") desc: Boolean
    ): Flow<ArticleCommentsList>

    @Headers("Content-Type: text/plain; charset=utf-8")
    @POST("api/board/{bid}/article/{aid}/rank")
    fun postArticleRank(
        @Path("bid") boardId: String,
        @Path("aid") articleId: String,
        @Body body: RequestBody
    ): Flow<ArticleRank>

    @Headers("Content-Type: text/plain; charset=utf-8")
    @GET("api/articles/popular")
    fun getPopularArticles(
        @Query("start_idx") startIndex: String,
        @Query("limit") limit: Int,
        @Query("desc") desc: Boolean,
    ): Flow<HotArticleList>

    @Headers("Content-Type: text/plain; charset=utf-8")
    @POST("api/board/{bid}/article/{aid}/comment")
    fun createArticleComment(
        @Path("bid") boardId: String,
        @Path("aid") articleId: String,
        @Body body: RequestBody
    ): Flow<ArticleComment>

    @Deprecated("")
    @GET("api/Rank/{bid}/{aid}")
    fun getArticleRank(
        @Path("bid") boardId: String,
        @Path("aid") articleId: String
    ): Flow<PostRank>
}
