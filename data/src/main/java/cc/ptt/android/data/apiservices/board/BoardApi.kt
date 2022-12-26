package cc.ptt.android.data.apiservices.board

import cc.ptt.android.data.model.remote.board.article.ArticleList
import cc.ptt.android.data.model.remote.board.hotboard.BoardList
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BoardApi {
    @GET("api/boards/popular")
    fun getPopularBoard(): Flow<BoardList>

    @GET("api/board/{bid}/articles")
    fun getArticles(
        @Path("bid") boardId: String,
        @Query("title") title: String,
        @Query("start_idx") startIndex: String,
        @Query("limit") limit: Int,
        @Query("desc") desc: Boolean
    ): Flow<ArticleList>

    @GET("api/boards")
    fun searchBoards(
        @Query("keyword") keyword: String,
        @Query("start_idx") start_idx: String,
        @Query("limit") limit: Int,
        @Query("asc") asc: Boolean
    ): Flow<BoardList>

    @GET("api/user/{user_id}/favorites")
    fun favoriteBoards(
        @Path("user_id") user_id: String,
        @Query("level_idx") level_idx: String,
        @Query("start_idx") start_idx: String,
        @Query("limit") limit: Int,
        @Query("asc") asc: Boolean
    ): Flow<BoardList>
}
