package wse18.ase.qse03.mobile.ui.appointmentOverview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import wse18.ase.qse03.mobile.model.Appointment
import wse18.ase.qse03.mobile.repository.AppointmentRepo
import java.util.*

class AppointmentOverviewViewModel : ViewModel() {
    var appointmentListLD: MutableLiveData<List<Appointment>> = MutableLiveData()
    var currentSelectedDate: MutableLiveData<Date> = MutableLiveData()
    var appointmentRegistrationSuccess: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var appointmentRepo: AppointmentRepo

    fun initAppointments(officeId: Long) {
        appointmentRepo = AppointmentRepo()
        val c = Calendar.getInstance()
        val date = c.time
        /*val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH) + 1
        val day = c.get(Calendar.DAY_OF_MONTH)
        val stringDay = "0$day"
        val stringMonth = "0$month"
        var stringDate = "" + day
        if (day < 10) {
            stringDate = stringDay
        }
        if (month < 10) {
            stringDate += "-$stringMonth"
        } else {
            stringDate += "-$month"
        }
        currentSelectedDate.value = stringDate + "-$year"*/
        currentSelectedDate.value = date
    }

    fun updateAppointmentList(officeId: Long, date: String) {
        appointmentRepo.getAllAvailableAppointmentsByOfficeAndDate(appointmentListLD, officeId, date)
    }

    fun signupForAppointment(appointment: Appointment, idToken: String?) {
        appointmentRepo.postSignupForAppointment(appointmentRegistrationSuccess, appointment, idToken)
    }
}
