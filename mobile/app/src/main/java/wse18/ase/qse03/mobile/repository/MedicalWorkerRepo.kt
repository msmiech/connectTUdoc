package wse18.ase.qse03.mobile.repository

import androidx.lifecycle.MutableLiveData
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import wse18.ase.qse03.mobile.model.MedicalWorker
import wse18.ase.qse03.mobile.service.MedicalWorkerService
import javax.inject.Singleton


@Singleton
class MedicalWorkerRepo {

    private val medWorkerService by lazy {
        MedicalWorkerService.create()
    }

    fun getAllMedicalWorkers(): MutableLiveData<List<MedicalWorker>> {
        val liveData: MutableLiveData<List<MedicalWorker>> = MutableLiveData()
        val cb = medWorkerService.findAllMedicalWorkers()
        cb.enqueue(object : Callback<List<MedicalWorker>> {
            override fun onResponse(call: Call<List<MedicalWorker>>?, response: Response<List<MedicalWorker>>?) {
                if (response != null && response.isSuccessful) {
                    var listMW = response.body()
                    Log.i("MedicalWorkerRepo", "Response body string: " + listMW.toString())
                    liveData.value = listMW
                } else {
                    Log.wtf(
                        "MedicalWorkerRepo::getAllMedicalWorkers",
                        "No data from callback findAllMedicalWorkers " + response?.errorBody()?.toString()
                    )
                }
            }

            override fun onFailure(call: Call<List<MedicalWorker>>?, t: Throwable?) {
                Log.wtf("MedicalWorkerRepo::getAllMedicalWorkers", "onFailure " + t.toString())
            }
        })

        return liveData
    }
}