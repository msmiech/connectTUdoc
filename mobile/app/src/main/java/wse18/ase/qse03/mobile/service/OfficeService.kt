package wse18.ase.qse03.mobile.service

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import wse18.ase.qse03.mobile.model.Office
import wse18.ase.qse03.mobile.model.RegistrationCode
import wse18.ase.qse03.mobile.util.RetrofitUtil

interface OfficeService {

    @GET("/office/{id}")
    fun findOfficeById(@Path("id") id: Long): Call<Office>

    @GET("/office")
    fun findAllOffices(): Call<List<Office>>

    @GET("/office/searchText/{searchText}")
    fun findOfficeByNameText(@Path("searchText") text: String): Call<List<Office>>

    @GET("/office/patientOffices")
    fun findOfficesByUser(@Header("X-Firebase-Auth") idToken: String?): Call<List<Office>>

    @GET("/office/patient/registrationcode")
    fun getRegistrationCode(@Header("X-Firebase-Auth") idToken: String?): Call<RegistrationCode>

    companion object {
        fun create(): OfficeService {

            return RetrofitUtil.getMedconnectRetrofit().create(OfficeService::class.java)
        }
    }
}