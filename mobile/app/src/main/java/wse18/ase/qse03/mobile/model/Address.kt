package wse18.ase.qse03.mobile.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class Address (
    var id: Long,
    var street: String,
    var number: String,
    var door: String,
    var floor: String,
    var place: String,
    var zip: Int,
    var city: String,
    var country: String

): Serializable {
    constructor() : this(-1, "", "", "", "", "", 1, "", "")
    override fun toString(): String = street + " " + number + "/" + floor + "/" + door + ",\n" + zip + " " + city + " " + country
    fun toSearchAddressString(): String = street + " " + number + ", " + city + ", " + country
}