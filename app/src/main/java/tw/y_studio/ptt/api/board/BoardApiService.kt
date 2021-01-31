package tw.y_studio.ptt.api.board

import retrofit2.http.GET
import tw.y_studio.ptt.api.model.hot_board.HotBoard

interface BoardApiService {
    @GET("api/boards/popular")
    suspend fun getPopularBoard(): HotBoard
}
