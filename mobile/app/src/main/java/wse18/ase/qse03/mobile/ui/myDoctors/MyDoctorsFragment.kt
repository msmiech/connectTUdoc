package wse18.ase.qse03.mobile.ui.myDoctors

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseUser
import wse18.ase.qse03.mobile.model.Office
import wse18.ase.qse03.mobile.adapter.OfficeAdapter
import wse18.ase.qse03.mobile.R
import wse18.ase.qse03.mobile.util.FirebaseUtil
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.GetTokenResult
import kotlinx.android.synthetic.main.my_doctors_fragment.view.*
import kotlinx.android.synthetic.main.personal_appointments_fragment.*
import wse18.ase.qse03.mobile.adapter.AppointmentListAdapter
import wse18.ase.qse03.mobile.model.RegistrationCode


class MyDoctorsFragment : Fragment() {

    private lateinit var viewModel: MyDoctorsViewModel
    private lateinit var doctorsListView: ListView
    private var dialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity!!.title = getString(R.string.my_doctors_label)
        FirebaseUtil.getFirebaseUser() ?: return inflater.inflate(R.layout.login_placeholder, container, false)
        val view: View = inflater.inflate(R.layout.my_doctors_fragment, container, false)
        doctorsListView = view.findViewById(R.id.myDoctors_listView)
        doctorsListView.adapter = OfficeAdapter(context!!, ArrayList())
        doctorsListView.setOnItemClickListener { _: AdapterView<*>?, innerView: View?, position: Int, _: Long ->
            val office = ((doctorsListView.adapter as OfficeAdapter).getItem(position) as Office)
            var bundle = Bundle()
            bundle.putSerializable(ChatFragment.ARG_PARAM1, office)
            Navigation.findNavController(innerView!!).navigate(R.id.action_myDoctors_dest_to_chat, bundle)
        }
        val builder = AlertDialog.Builder(context)
        dialog = builder.create()
        val addButton : Button = view.findViewById(R.id.myDoctor_add)
        addButton.setOnClickListener {
            FirebaseUtil.getFirebaseUser()!!.getIdToken(false).addOnSuccessListener(OnSuccessListener<GetTokenResult> {
                viewModel.getRegistrationcode(it.token)
            })
            val registerForCodeObserver = Observer<RegistrationCode> { code ->
                if(code != null && code.code != null) {
                    if(!this.dialog!!.isShowing) {
                        this.dialog!!.setTitle(R.string.mydoctor_code_description)
                        this.dialog!!.setMessage("" + code.code)
                        this.dialog!!.show()
                        viewModel.registrationCode.value = null
                    }
                }
            }
            viewModel.registrationCode.observe(this, registerForCodeObserver)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MyDoctorsViewModel::class.java)
        viewModel.init()
        val patientOfficesObserver = Observer<List<Office>> { patientOffices ->
            val noDoctorsText = view?.findViewById<TextView>(R.id.no_doctors_label)
            if (patientOffices != null && patientOffices.isNotEmpty()) {
                (doctorsListView.adapter as OfficeAdapter).updateData(patientOffices)
                noDoctorsText?.visibility = View.INVISIBLE
            }else{
                noDoctorsText?.visibility = View.VISIBLE
            }
        }
        viewModel.patientOfficesLD.observe(this, patientOfficesObserver)
        val user: FirebaseUser? = FirebaseUtil.getFirebaseUser()
        if (user != null) {
            user.getIdToken(false).addOnSuccessListener(OnSuccessListener<GetTokenResult> {
                viewModel.updateOfficeList(it.token)
            })
        }
    }
}