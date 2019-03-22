package wse18.ase.qse03.mobile.repository

import android.util.Log
import wse18.ase.qse03.mobile.model.Medicine
import wse18.ase.qse03.mobile.service.MedicineService
import javax.inject.Singleton
import androidx.lifecycle.MutableLiveData
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Singleton
class MedicineRepo {
    private val medicineService by lazy {
        MedicineService.create()
    }

    fun getMedicineForId(medicine: MutableLiveData<Medicine>, idToken: String?, id: String?) {
        val cb = medicineService.findMedicineFromBarcode(idToken,id)
        cb.enqueue(object : Callback<Medicine> {
            override fun onResponse(call: Call<Medicine>?, response: Response<Medicine>?) {
                if (response != null && response.isSuccessful) {
                    medicine.value = response.body()
                    Log.i("MedicineRepo", "Response body string: " + medicine.toString())
                } else {
                    medicine.value = null
                    Log.wtf(
                        "MedicineRepot::getMedicineForId",
                        "No data from callback getMedicineForId " + response?.errorBody()?.toString()
                    )
                }
            }

            override fun onFailure(call: Call<Medicine>?, t: Throwable?) {
                Log.wtf("MedicineRepo::getMedicineForId", "onFailure " + t.toString())
            }
        })
    }
}