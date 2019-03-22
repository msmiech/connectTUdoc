package wse18.ase.qse03.mobile.repository

import androidx.lifecycle.MutableLiveData
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import wse18.ase.qse03.mobile.model.Office
import wse18.ase.qse03.mobile.model.RegistrationCode
import wse18.ase.qse03.mobile.service.OfficeService
import javax.inject.Singleton

@Singleton
class OfficeRepo {

    private val officeService by lazy {
        OfficeService.create()
    }

    fun getOfficeById(office: MutableLiveData<Office>, id: Long){
        val cb = officeService.findOfficeById(id)
        cb.enqueue(object : Callback<Office> {
            override fun onResponse(call: Call<Office>?, response: Response<Office>?) {
                if (response != null && response.isSuccessful) {
                    var officeResult = response.body()
                    Log.i("OfficeRepo", "Response body string: " + officeResult.toString())
                    office.value = officeResult
                } else {
                    Log.wtf("OfficeRepo::getOfficeById", "No data from callback findOfficeById " + response?.errorBody()?.toString())
                }
            }

            override fun onFailure(call: Call<Office>?, t: Throwable?) {
                Log.wtf("OfficeRepo::getOfficeById", "onFailure " + t.toString())
            }
        })
    }

    fun getAllOffices(officeList: MutableLiveData<List<Office>>){
        val cb = officeService.findAllOffices()
        cb.enqueue(object : Callback<List<Office>> {
            override fun onResponse(call: Call<List<Office>>?, response: Response<List<Office>>?) {
                if (response != null && response.isSuccessful) {
                    var listOffice = response.body()
                    Log.i("OfficeRepo", "Response body string: " + listOffice.toString())
                    officeList.value = listOffice
                } else {
                    Log.wtf("OfficeRepo::getAllOffices", "No data from callback findAllOffices " + response?.errorBody()?.toString())
                }
            }

            override fun onFailure(call: Call<List<Office>>?, t: Throwable?) {
                Log.wtf("OfficeRepo::getAllOffices", "onFailure " + t.toString())
            }
        })
    }

    fun getOfficeBySearchText(officeList: MutableLiveData<List<Office>>, text: String) {
        val cb = officeService.findOfficeByNameText(text)
        cb.enqueue(object : Callback<List<Office>> {
            override fun onResponse(call: Call<List<Office>>?, response: Response<List<Office>>?) {
                if (response != null && response.isSuccessful) {
                    var listOffice = response.body()
                    Log.i("OfficeRepo", "Response body string: " + listOffice.toString())
                    officeList.value = listOffice
                } else {
                    Log.wtf("OfficeRepo::getOfficeBySearchText", "No data from callback findOfficeByNameText " + response?.errorBody()?.toString())
                }
            }

            override fun onFailure(call: Call<List<Office>>?, t: Throwable?) {
                Log.wtf("OfficeRepo::getOfficeBySearchText", "onFailure " + t.toString())
            }
        })
    }

    fun getOfficesByUser(officeList: MutableLiveData<List<Office>>, idToken: String?) {
        var cb = officeService.findOfficesByUser(idToken)
        cb.enqueue(object : Callback<List<Office>> {
            override fun onResponse(call: Call<List<Office>>?, response: Response<List<Office>>?) {
                if (response != null && response.isSuccessful) {
                    var listOffice = response.body()
                    Log.i("OfficeRepo", "Response body string: " + listOffice.toString())
                    officeList.value = listOffice
                } else {
                    Log.wtf("OfficeRepo::getOfficesByUser", "No data from callback findOfficesByUser " + response?.errorBody()?.toString())
                }
            }

            override fun onFailure(call: Call<List<Office>>?, t: Throwable?) {
                Log.wtf("OfficeRepo::getOfficesByUser", "onFailure " + t.toString())
            }
        })
    }

    fun getRegistrationCode(registrationCode: MutableLiveData<RegistrationCode>, idToken: String?) {
        val cb = officeService.getRegistrationCode(idToken);
        cb.enqueue(object : Callback<RegistrationCode> {
            override fun onResponse(call: Call<RegistrationCode>?, response: Response<RegistrationCode>?) {
                if (response != null && response.isSuccessful) {
                    registrationCode.value = response.body()
                    Log.i("OfficeRepo", "Response body string: " + registrationCode.toString())
                } else {
                    registrationCode.value = null
                    Log.wtf(
                        "MedicineRepot::getMedicineForId",
                        "No data from callback getMedicineForId " + response?.errorBody()?.toString()
                    )
                }
            }

            override fun onFailure(call: Call<RegistrationCode>?, t: Throwable?) {
                Log.wtf("OfficeRepo::getRegistrationCode", "onFailure " + t.toString())
            }
        })
    }
}