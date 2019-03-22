package wse18.ase.qse03.mobile.model

import java.io.Serializable

class OfficeHour (
    val id: Long,
    var beginTime: String,
    var endTime: String,
    var daytype: String
): Serializable {
    constructor() : this(-1, "", "", "")
    override fun toString(): String = daytype.substring(0,1) + daytype.substring(1,3).toLowerCase() + ": " + beginTime + " - " + endTime
}