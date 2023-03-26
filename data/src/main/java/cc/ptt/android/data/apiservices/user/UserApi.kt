package cc.ptt.android.data.apiservices.user

import cc.ptt.android.common.network.api.ApiMaxLogLevel
import cc.ptt.android.common.network.api.MaxLogLevel
import cc.ptt.android.data.model.remote.user.exist_user.ExistUser
import cc.ptt.android.data.model.remote.user.login.LoginEntity
import cc.ptt.android.data.model.remote.user.user_id.UserIdEntity
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

@ApiMaxLogLevel(MaxLogLevel.HEADERS)
interface UserApi {

    @Headers("Content-Type: text/plain; charset=utf-8")
    @POST("api/account/login/")
    fun login(@Body body: RequestBody): Flow<LoginEntity>

    @Headers("Content-Type: text/plain; charset=utf-8")
    @POST("api/account/existsuser/")
    fun existUser(@Body body: RequestBody): Flow<ExistUser>

    @Headers("Content-Type: text/plain; charset=utf-8")
    @GET("api/userid/")
    fun userId(): Flow<UserIdEntity>
}
