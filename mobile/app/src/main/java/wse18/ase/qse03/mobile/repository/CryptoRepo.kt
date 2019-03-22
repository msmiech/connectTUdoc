package wse18.ase.qse03.mobile.repository

import androidx.lifecycle.MutableLiveData
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import wse18.ase.qse03.mobile.model.VirgilToken
import wse18.ase.qse03.mobile.service.VirgilJWTService
import javax.inject.Singleton

@Singleton
class CryptoRepo {

    private val virgilJWTService by lazy {
        VirgilJWTService.create()
    }

    fun getVirgilToken(token: MutableLiveData<VirgilToken>, idToken: String?){

        val cb = virgilJWTService.getToken(idToken)
        cb.enqueue(object : Callback<VirgilToken> {
            override fun onResponse(call: Call<VirgilToken>?, response: Response<VirgilToken>?) {
                if (response != null && response.isSuccessful) {
                    val t = response.body()
                    Log.i("CryptoRepo", "Response body string: " + t.toString())
                    token.value = t
                } else {
                    Log.wtf("CryptoRepo::getVirgilToken", "No data from callback getToken " + response?.code())
                }
            }

            override fun onFailure(call: Call<VirgilToken>?, t: Throwable?) {
                Log.wtf("UserRepo::getVirgilToken", "onFailure " + t.toString())
            }
        })
    }
}