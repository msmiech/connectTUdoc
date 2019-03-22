package wse18.ase.qse03.mobile.ui.appointmentOverview

import android.app.AlertDialog
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
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.appointment_overview_fragment.*
import wse18.ase.qse03.mobile.adapter.AppointmentListAdapter

import wse18.ase.qse03.mobile.R
import wse18.ase.qse03.mobile.model.Appointment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "officeId"

class AppointmentOverviewFragment : Fragment() {

    private lateinit var viewModel: AppointmentOverviewViewModel
    private lateinit var appointmentListView: ListView
    private var officeId: Long? = null
    private val APPOINTMENT_OVERVIEW = "appointment-overview"
    private val dateFormatTextDisplay: DateFormat = SimpleDateFormat("dd.MM.yyyy")
    private val dateFormatBackend: DateFormat = SimpleDateFormat("dd-MM-yyyy")
    private var appointmentRegistrationInProgress: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            officeId = it.getLong(ARG_PARAM1)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.appointment_overview_fragment, container, false)
        appointmentListView = view.findViewById(R.id.appointment_listView)
        appointmentListView.adapter = AppointmentListAdapter(context!!, ArrayList(), APPOINTMENT_OVERVIEW)
        appointmentListView.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            val appointment =
                ((appointment_listView.adapter as AppointmentListAdapter).getItem(position) as Appointment)
            val builder = AlertDialog.Builder(context)
            builder.setMessage(R.string.dialog_sign_up_for_appointment)
                .setPositiveButton(
                    android.R.string.yes
                ) { dialog, id ->
                    Log.i("Appointments", appointment.toString())
                    val auth = FirebaseAuth.getInstance()
                    if (auth.currentUser != null) {
                        val user = auth.currentUser
                        val idToken: String? = user!!.getIdToken(false).result?.token
                        viewModel.signupForAppointment(appointment, idToken)
                        appointmentRegistrationInProgress = true
                    } else {
                        //TODO show alert that user is not logged in
                    }

                }
                .setNegativeButton(
                    android.R.string.no
                ) { dialog, id ->
                    Log.i("Appointments", "Did not sign up for appointment")
                }
            // Create the AlertDialog object and return it
            val dialog = builder.create()
            dialog.show()
        }
        val dateTextView = view.findViewById<TextView>(R.id.appointment_date)
        dateTextView.setOnClickListener { v -> showDatePickerDialog(v) }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(AppointmentOverviewViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        initAppointmentListObserver()
        val appointmentDateObserver = Observer<Date> { date ->
            Log.d("LOG_TAG", "Selected date changed to: $date")
            if (date != null) {
                val dateTextView = view?.findViewById<TextView>(R.id.appointment_date)

                dateTextView?.text = dateFormatTextDisplay.format(date) // for display the date text format is different
                viewModel.updateAppointmentList(officeId!!, dateFormatBackend.format(date))
            }
        }
        viewModel.currentSelectedDate.observe(this, appointmentDateObserver)
        viewModel.initAppointments(officeId!!)

        val registerForAppointmentObserver = Observer<Boolean> { success ->
            Log.d("LOG_TAG", "Appointment registered: $success")
            if (success != null && appointmentRegistrationInProgress) {
                if(success == true){
                    val selectedDate = SimpleDateFormat("dd-MM-yyyy").format(viewModel.currentSelectedDate.value)
                    viewModel.updateAppointmentList(officeId!!, selectedDate)
                    val toast = Toast.makeText(context, R.string.appointment_registered, Toast.LENGTH_SHORT)
                    toast.show()
                }else{
                    val toast = Toast.makeText(context, R.string.appointment_not_registered, Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
            appointmentRegistrationInProgress = false
        }
        viewModel.appointmentRegistrationSuccess.observe(this, registerForAppointmentObserver)

    }

    private fun initAppointmentListObserver() {
        val appointmentListObserver = Observer<List<Appointment>> { appointments ->
            Log.d("LOG_TAG", "Appointment list updated. Number of appointments: " + appointments.size)
            val noAppointmentsText = view?.findViewById<TextView>(R.id.no_appointments_label)
            if (appointments != null && appointments.isNotEmpty()) {
                (appointment_listView.adapter as AppointmentListAdapter).updateData(appointments)
                noAppointmentsText?.visibility = View.INVISIBLE
            } else {
                (appointment_listView.adapter as AppointmentListAdapter).updateData(emptyList<Appointment>())
                noAppointmentsText?.visibility = View.VISIBLE
            }
        }
        viewModel.appointmentListLD.observe(this, appointmentListObserver)

    }

    fun showDatePickerDialog(v: View) {
        val newFragment = DatePickerFragment()
        newFragment.show(fragmentManager, "datePicker")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of AppointmentOverviewFragment.
         */
        @JvmStatic
        fun newInstance(param1: Long) =
            AppointmentOverviewFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM1, param1)
                }
            }
    }

}
