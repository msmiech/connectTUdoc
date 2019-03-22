package wse18.ase.qse03.mobile.model

import java.io.Serializable

data class Speciality(
    var id: Long,
    var specialityName: String
): Serializable {
    constructor() : this(-1, "")
}