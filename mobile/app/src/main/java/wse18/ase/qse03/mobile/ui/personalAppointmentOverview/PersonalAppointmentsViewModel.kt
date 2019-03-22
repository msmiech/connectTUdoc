package wse18.ase.qse03.mobile.ui.personalAppointmentOverview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import wse18.ase.qse03.mobile.model.Appointment
import wse18.ase.qse03.mobile.model.User
import wse18.ase.qse03.mobile.repository.AppointmentRepo

class PersonalAppointmentsViewModel : ViewModel() {
    var appointmentListLD : MutableLiveData<List<Appointment>> = MutableLiveData()
    var appointmentRepo: AppointmentRepo = AppointmentRepo()


    fun updateAppointmentList(idToken: String?) {
        appointmentRepo.getAllAppointmentsByPatient(appointmentListLD, idToken)
    }

}
