package tw.y_studio.ptt.api.article

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tw.y_studio.ptt.api.model.article.ArticleCommentsList
import tw.y_studio.ptt.api.model.article.ArticleDetail

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
}
