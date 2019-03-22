package wse18.ase.qse03.mobile.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Medicine (
    var id: String,
    var name: String

){
    constructor() : this("", "")
    override fun toString(): String = name
}