package wse18.ase.qse03.mobile.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class MedicalWorker (
    var id: Long,
    var uid: String,
    var preTitle: String?,
    var firstName: String,
    var lastName: String,
    var posTitle: String?,
    var eMail: String?,
    var privateKey: String?,
    var publicKey: String?,
    var speciality: List<Speciality>?,
    var type: MedWorkerType
): Serializable {

    constructor() : this(-1, "","", "", "", "", "", "", "", emptyList<Speciality>(), MedWorkerType.DOCTOR)
}