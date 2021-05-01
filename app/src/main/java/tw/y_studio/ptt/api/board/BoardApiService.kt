package tw.y_studio.ptt.api.board

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tw.y_studio.ptt.api.model.board.article.ArticleList
import tw.y_studio.ptt.api.model.board.hot_board.HotBoard

interface BoardApiService {
    @GET("api/boards/popular")
    suspend fun getPopularBoard(): HotBoard

    @GET("api/board/{bid}/articles")
    suspend fun getArticles(
        @Path("bid") boardId: String,
        @Query("title") title: String,
        @Query("start_idx") startIndex: String,
        @Query("limit") limit: Int,
        @Query("desc") desc: Boolean
    ): ArticleList

    @GET("api/boards")
    suspend fun searchBoards(
        @Query("keyword") keyword: String,
        @Query("start_idx") start_idx: String,
        @Query("limit") limit: Int,
        @Query("asc") asc: Boolean
    ): HotBoard
}
