package wse18.ase.qse03.mobile.model

data class SDPMessage(val type: String,val sdp: String) : ClientMessage(MessageType.SDPMessage) {
    constructor() : this("","")
}