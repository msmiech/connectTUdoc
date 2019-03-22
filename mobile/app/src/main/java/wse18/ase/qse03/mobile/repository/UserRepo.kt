package wse18.ase.qse03.mobile.repository

import androidx.lifecycle.MutableLiveData
import android.util.Log
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import wse18.ase.qse03.mobile.model.User
import wse18.ase.qse03.mobile.service.UserService
import javax.inject.Singleton

@Singleton
class UserRepo {

    private val userService by lazy {
        UserService.create()
    }

    fun getLoggedInUser(user: MutableLiveData<User>, idToken: String?){

        val cb = userService.getCurrentUser(idToken)
        cb.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                if (response != null && response.isSuccessful) {
                    val u = response.body()
                    Log.i("UserRepo", "Response body string: " + u.toString())
                    user.value = u
                } else {
                    Log.wtf("UserRepo::getLoggedInUser", "No data from callback getCurrentUser " + response?.code())
                }
            }

            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.wtf("UserRepo::getLoggedInUser", "onFailure " + t.toString())
            }
        })
    }

    fun subscribeToNotifications(idToken: String?, registrationToken: String){
        val requestBodyToken = RequestBody.create(MediaType.parse("text/plain"), registrationToken)
        val cb = userService.subscribeToNotifications(idToken, requestBodyToken)
        cb.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                if (response != null && response.isSuccessful) {
                    val u = response.body()
                    Log.i("UserRepo", "Response body string: " + u.toString())
                } else {
                    Log.wtf("UserRepo::subscribe", "No data from callback subscribeToNotifications " + response?.code())
                }
            }

            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                Log.wtf("UserRepo::subscribe", "onFailure " + t.toString())
            }
        })
    }
}