package tw.y_studio.ptt.api.article

import okhttp3.RequestBody
import retrofit2.http.*
import tw.y_studio.ptt.api.model.article.ArticleCommentsList
import tw.y_studio.ptt.api.model.article.ArticleDetail
import tw.y_studio.ptt.api.model.article.ArticleRank

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
}
