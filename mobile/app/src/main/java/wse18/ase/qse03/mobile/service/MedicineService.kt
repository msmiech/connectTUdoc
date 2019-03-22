package wse18.ase.qse03.mobile.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import wse18.ase.qse03.mobile.model.Medicine
import wse18.ase.qse03.mobile.util.RetrofitUtil

interface MedicineService {

    @GET("/medicine/{id}")
    fun findMedicineFromBarcode(@Header("X-Firebase-Auth") idToken: String?, @Path("id") id: String?): Call<Medicine>

    companion object {
        fun create(): MedicineService {
            return RetrofitUtil.getMedconnectRetrofit().create(MedicineService::class.java)
        }
    }
}