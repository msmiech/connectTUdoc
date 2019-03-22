package wse18.ase.qse03.mobile.service

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import wse18.ase.qse03.mobile.model.MedicalWorker
import wse18.ase.qse03.mobile.util.RetrofitUtil


interface MedicalWorkerService {

    @GET("/medicalworker/")
    fun findAllMedicalWorkers(): Call<List<MedicalWorker>>

    companion object {
        fun create(): MedicalWorkerService {

            return RetrofitUtil.getMedconnectRetrofit().create(MedicalWorkerService::class.java)
        }
    }
}