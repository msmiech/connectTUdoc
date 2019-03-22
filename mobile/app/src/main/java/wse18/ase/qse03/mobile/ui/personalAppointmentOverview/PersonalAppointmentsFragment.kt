package wse18.ase.qse03.mobile.ui.personalAppointmentOverview

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.appointment_overview_fragment.*
import kotlinx.android.synthetic.main.personal_appointments_fragment.*

import wse18.ase.qse03.mobile.R
import wse18.ase.qse03.mobile.adapter.AppointmentListAdapter
import wse18.ase.qse03.mobile.model.Appointment
import wse18.ase.qse03.mobile.util.FirebaseUtil
import java.util.ArrayList

class PersonalAppointmentsFragment : Fragment() {

    companion object {
        fun newInstance() = PersonalAppointmentsFragment()
    }

    private lateinit var appointmentListView: ListView
    private lateinit var viewModel: PersonalAppointmentsViewModel
    private val PERSONAL_APPOINTMENTS = "personal_appointments"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity!!.title = getString(R.string.my_appointments_label)
        FirebaseUtil.getFirebaseUser() ?: return inflater.inflate(R.layout.login_placeholder, container, false)
        val view = inflater.inflate(R.layout.personal_appointments_fragment, container, false)
        appointmentListView = view.findViewById(R.id.personal_appointments_listView)
        appointmentListView.adapter = AppointmentListAdapter(context!!, ArrayList(), PERSONAL_APPOINTMENTS)
        appointmentListView.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            //TODO
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PersonalAppointmentsViewModel::class.java)
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            val user = auth.currentUser
            val idToken: String? = user!!.getIdToken(false).result?.token
            viewModel.updateAppointmentList(idToken)
        }

        val appointmentListObserver = Observer<List<Appointment>> { appointments ->
            Log.d("LOG_TAG", "Appointment list updated. Number of appointments: " + appointments.size)
            val noAppointmentsText = view?.findViewById<TextView>(R.id.no_personal_appointments_label)
            if (appointments != null && appointments.isNotEmpty()) {
                (personal_appointments_listView.adapter as AppointmentListAdapter).updateData(appointments)
                noAppointmentsText?.visibility = View.INVISIBLE
            }else{
                noAppointmentsText?.visibility = View.VISIBLE
            }
        }
        viewModel.appointmentListLD.observe(this, appointmentListObserver)
    }

}
