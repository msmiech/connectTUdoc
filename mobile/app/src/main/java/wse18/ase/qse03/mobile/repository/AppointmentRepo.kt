package wse18.ase.qse03.mobile.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import wse18.ase.qse03.mobile.model.Appointment
import wse18.ase.qse03.mobile.model.User
import wse18.ase.qse03.mobile.service.AppointmentService
import java.util.*

class AppointmentRepo {
    private val appointmentService by lazy {
        AppointmentService.create()
    }

    fun getAllAvailableAppointmentsByOfficeAndDate(
        appointmentList: MutableLiveData<List<Appointment>>,
        officeId: Long,
        date: String
    ) {
        val cb = appointmentService.findAllAvailableAppointmentsByDate(officeId, date)
        cb.enqueue(object : Callback<List<Appointment>> {
            override fun onResponse(call: Call<List<Appointment>>?, response: Response<List<Appointment>>?) {
                if (response != null && response.isSuccessful) {
                    var appointments = response.body()
                    Log.i("AppointmentRepo", "Response body string: " + appointments.toString())
                    appointmentList.value = appointments
                } else {
                    Log.wtf(
                        "AppointmentRepo::getAllAvailableAppointmentsByDate",
                        "No data from callback findAllAvailableAppointmentsByDate() " + response?.errorBody()?.toString()
                    )
                }
            }

            override fun onFailure(call: Call<List<Appointment>>?, t: Throwable?) {
                Log.wtf("AppointmentRepo::getAllAvailableAppointmentsByDate", "onFailure " + t.toString())
            }
        })
    }

    fun getAllAppointmentsByPatient(appointmentList: MutableLiveData<List<Appointment>>, idToken: String?) {
        val cb = appointmentService.findAllAppointmentsByPatient(idToken)
        cb.enqueue(object : Callback<List<Appointment>> {
            override fun onResponse(call: Call<List<Appointment>>?, response: Response<List<Appointment>>?) {
                if (response != null && response.isSuccessful) {
                    var appointments = response.body()
                    Log.i("AppointmentRepo", "Response body string: " + appointments.toString())
                    appointmentList.value = appointments
                } else {
                    Log.wtf(
                        "AppointmentRepo::findAllAppointmentsByPatient",
                        "No data from callback findAllAppointmentsByPatient() " + response?.errorBody()?.toString()
                    )
                }
            }

            override fun onFailure(call: Call<List<Appointment>>?, t: Throwable?) {
                Log.wtf("AppointmentRepo::findAllAppointmentsByPatient", "onFailure " + t.toString())
            }
        })
    }

    fun postSignupForAppointment(success: MutableLiveData<Boolean>, appointment: Appointment, idToken: String?) {
        val cb = appointmentService.signUpForAppointment(idToken, appointment)
        cb.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                if (response != null && response.isSuccessful) {
                    Log.i("AppointmentRepo", "Response body string: " + response.toString())
                    success.value = true
                } else {
                    Log.wtf(
                        "AppointmentRepo::postSignupForAppointment",
                        "No data from callback postSignupForAppointment() " + response?.errorBody()?.toString()
                    )
                    success.value = false
                }
            }

            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                Log.wtf("AppointmentRepo::postSignupForAppointment", "onFailure " + t.toString())
            }
        })
    }
}