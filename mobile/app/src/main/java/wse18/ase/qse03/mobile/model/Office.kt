package wse18.ase.qse03.mobile.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class Office(
    var id: Long,
    var name: String,
    var phone: String,
    var fax: String,
    var address: Address,
    var email: String,
    var officeWorkers: List<MedicalWorker>,
    var officehours: List<OfficeHour>
) : Serializable {
    constructor() : this(-1, "", "", "", Address(), "", emptyList<MedicalWorker>(), emptyList<OfficeHour>())
}