package cc.ptt.android.data.api.board

import cc.ptt.android.data.model.remote.board.article.ArticleList
import cc.ptt.android.data.model.remote.board.hotboard.HotBoard
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("api/user/{user_id}/favorites")
    suspend fun favoriteBoards(
        @Path("user_id") user_id: String,
        @Query("level_idx") level_idx: String,
        @Query("start_idx") start_idx: String,
        @Query("limit") limit: Int,
        @Query("asc") asc: Boolean
    ): HotBoard
}
