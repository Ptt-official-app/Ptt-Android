package cc.ptt.android.data.api.user

import cc.ptt.android.data.model.remote.user.exist_user.ExistUser
import cc.ptt.android.data.model.remote.user.login.Login
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserApiService {
    @Headers("Content-Type: text/plain; charset=utf-8")
    @POST("api/account/login/")
    suspend fun login(@Body body: RequestBody): Login

    @Headers("Content-Type: text/plain; charset=utf-8")
    @POST("api/account/existsuser/")
    suspend fun existUser(@Body body: RequestBody): ExistUser
}
