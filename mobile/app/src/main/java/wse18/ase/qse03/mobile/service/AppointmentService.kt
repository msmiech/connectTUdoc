package wse18.ase.qse03.mobile.service

import retrofit2.Call
import retrofit2.http.*
import wse18.ase.qse03.mobile.model.Appointment
import wse18.ase.qse03.mobile.util.RetrofitUtil

interface AppointmentService {

    @GET("/appointment/available/{officeId}/{date}")
    fun findAllAvailableAppointmentsByDate(@Path("officeId") officeId: Long, @Path("date") date: String): Call<List<Appointment>>

    @GET("/appointment/patient/")
    fun findAllAppointmentsByPatient(@Header("X-Firebase-Auth") idToken: String?): Call<List<Appointment>>

    @POST("/appointment") // TODO
    fun signUpForAppointment(@Header("X-Firebase-Auth") idToken: String?, @Body appointment: Appointment): Call<Boolean>


    companion object {
        fun create(): AppointmentService {
            return RetrofitUtil.getMedconnectRetrofit().create(AppointmentService::class.java)
        }
    }
}