package wse18.ase.qse03.mobile.service

import retrofit2.Call
import retrofit2.http.*
import wse18.ase.qse03.mobile.model.VirgilToken
import wse18.ase.qse03.mobile.util.RetrofitUtil

interface VirgilJWTService {

    @GET("virgil-jwt")
    fun getToken(@Header("Authorization") firebaseToken: String?): Call<VirgilToken>


    companion object {
        fun create(): VirgilJWTService {
            return RetrofitUtil.getFirebaseFunctionRetrofit().create(VirgilJWTService::class.java)
        }
    }
}