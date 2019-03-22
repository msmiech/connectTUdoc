package wse18.ase.qse03.mobile.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class Appointment(
    var id: Long?,
    var office: Office,
    var patient: User?,
    var patientName: String?,
    var appointmentBegin: Date,
    var appointmentEnd: Date
): Serializable {
    constructor() : this(-1, Office(), User(), "", Date(), Date())
}