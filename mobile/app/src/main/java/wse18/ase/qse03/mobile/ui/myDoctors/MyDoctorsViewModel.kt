package wse18.ase.qse03.mobile.ui.myDoctors

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import wse18.ase.qse03.mobile.model.Office
import wse18.ase.qse03.mobile.model.RegistrationCode
import wse18.ase.qse03.mobile.repository.OfficeRepo

class MyDoctorsViewModel : ViewModel() {

    var patientOfficesLD: MutableLiveData<List<Office>> = MutableLiveData<List<Office>>()
    lateinit var officeRepo: OfficeRepo
    var registrationCode: MutableLiveData<RegistrationCode> = MutableLiveData()


    fun init() {
        officeRepo = OfficeRepo()
    }

    fun updateOfficeList(idToken: String?) {
        officeRepo.getOfficesByUser(patientOfficesLD, idToken)
    }

    fun getRegistrationcode(idToken: String?)  {
        val registrationCode = officeRepo.getRegistrationCode(registrationCode, idToken)
    }

}