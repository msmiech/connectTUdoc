package wse18.ase.qse03.mobile.ui.doctorSearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import wse18.ase.qse03.mobile.model.Office
import wse18.ase.qse03.mobile.repository.OfficeRepo


class DoctorSearchViewModel : ViewModel() {

    var officeListLD: MutableLiveData<List<Office>> = MutableLiveData()
    lateinit var officeRepo: OfficeRepo


    fun init() {
        officeRepo = OfficeRepo()
        officeRepo.getAllOffices(officeListLD)
    }

    fun updateOfficeList(searchText: String) {
        officeRepo.getOfficeBySearchText(officeListLD, searchText)
    }

}
