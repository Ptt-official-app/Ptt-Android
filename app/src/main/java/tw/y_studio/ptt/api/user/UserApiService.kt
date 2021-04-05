package tw.y_studio.ptt.api.user

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import tw.y_studio.ptt.api.model.user.Login

interface UserApiService {
    @Headers("Content-Type: text/plain; charset=utf-8")
    @POST("api/account/login/")
    suspend fun login(@Body body: RequestBody): Login
}
