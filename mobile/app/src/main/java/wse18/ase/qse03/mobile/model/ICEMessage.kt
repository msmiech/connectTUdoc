package wse18.ase.qse03.mobile.model

data class ICEMessage(
    val sdpMLineIndex: Int,
    val sdpMid: String,
    val candidate: String) : ClientMessage(MessageType.ICEMessage) {

    constructor() : this(-1,"","")
}