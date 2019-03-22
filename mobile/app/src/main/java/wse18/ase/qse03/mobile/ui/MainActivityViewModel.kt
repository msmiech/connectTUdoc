package wse18.ase.qse03.mobile.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import wse18.ase.qse03.mobile.model.Office
import wse18.ase.qse03.mobile.model.User
import wse18.ase.qse03.mobile.model.VirgilToken
import wse18.ase.qse03.mobile.repository.CryptoRepo
import wse18.ase.qse03.mobile.repository.OfficeRepo
import wse18.ase.qse03.mobile.repository.UserRepo
import wse18.ase.qse03.mobile.ui.myDoctors.SignalingFirebase


class MainActivityViewModel : ViewModel() {

    var user: MutableLiveData<User> = MutableLiveData()
    var office: MutableLiveData<Office> = MutableLiveData()
    var virgilToken: MutableLiveData<VirgilToken> = MutableLiveData()
    lateinit var userRepo: UserRepo
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var channelName = "/patient/"
    private var databaseReference: DatabaseReference? = null
    private var signalingFirebase: SignalingFirebase? = null
    lateinit var cryptoRepo: CryptoRepo
    lateinit var officeRepo: OfficeRepo

    fun init() {
        userRepo = UserRepo()
        cryptoRepo = CryptoRepo()
        officeRepo = OfficeRepo()
    }

    fun getOfficeById(id: Long) {
        officeRepo.getOfficeById(office, id)
    }

    fun getLoggedInUser(idToken: String?) {
        userRepo.getLoggedInUser(user, idToken)
    }

    fun setChannel(patientId: Long) {
        this.channelName = "/patient/" + patientId
        Log.e("Channel",this.channelName)
    }

    fun connectDatabaseReference() {
        databaseReference = this.database.getReference(this.channelName)
        if(databaseReference != null) {
            this.signalingFirebase = SignalingFirebase(databaseReference!!)
            if(this.signalingFirebase == null) {
                Log.e("MainActivityViewModel","Signalfirebase is null")
            }
        } else {
            Log.e("MainActivityViewModel","Cannot listen to incoming calls")
        }
    }

    fun getDatabaseReference() :DatabaseReference? {
        return this.databaseReference
    }
    fun getVirgilToken(idToken: String?) {
        cryptoRepo.getVirgilToken(virgilToken, idToken)
    }

    fun subscribeToNotifications(idToken: String?, registrationToken: String) {
        userRepo.subscribeToNotifications(idToken, registrationToken)
    }

    fun startListenOnIncomingCalls() {
        this.signalingFirebase?.listen(this.channelName)
    }

    fun stopListingOnIncomingCalls() {
        this.signalingFirebase?.close()
    }

    fun getSignaler(): SignalingFirebase? {
        return this.signalingFirebase
    }
}
