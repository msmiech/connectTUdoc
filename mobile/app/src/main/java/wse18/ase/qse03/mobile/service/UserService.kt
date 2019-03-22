package wse18.ase.qse03.mobile.service

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import wse18.ase.qse03.mobile.model.User
import wse18.ase.qse03.mobile.util.RetrofitUtil

interface UserService {

    @GET("/patient/current")
    fun getCurrentUser(@Header("X-Firebase-Auth") idToken: String?): Call<User>

    @POST("/patient/subscribe")
    fun subscribeToNotifications(@Header("X-Firebase-Auth") idToken: String?, @Body registrationToken: RequestBody): Call<Boolean>

    companion object {
        fun create(): UserService {
            return RetrofitUtil.getMedconnectRetrofit().create(UserService::class.java)
        }
    }
}