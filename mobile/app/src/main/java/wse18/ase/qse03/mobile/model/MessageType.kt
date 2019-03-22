package wse18.ase.qse03.mobile.model

enum class MessageType(val value: String) {
    SDPMessage("sdp"),
    ICEMessage("ice"),
    PeerLeft("peer-left");
    override fun toString() = value
}