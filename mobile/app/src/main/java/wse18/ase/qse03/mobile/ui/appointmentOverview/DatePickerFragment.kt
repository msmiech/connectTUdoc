package wse18.ase.qse03.mobile.ui.appointmentOverview

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DatePickerFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DatePickerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var appointmentViewModel: AppointmentOverviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appointmentViewModel = activity?.run {
            ViewModelProviders.of(this).get(AppointmentOverviewViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context!!, this, year, month, day)
        c.add(Calendar.DATE, -1)
        datePickerDialog.datePicker.minDate = c.timeInMillis
        return datePickerDialog
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        /* Do something with the date chosen by the use
        val stringDay = "0$day"
        val realMonth = month + 1
        val stringMonth = "0$realMonth"
        var stringDate = "" + day
        if (day < 10) {
            stringDate = stringDay
        }
        if (month < 10) {
            stringDate += "-$stringMonth"
        } else {
            stringDate += "-$realMonth"
        }*/
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        appointmentViewModel.currentSelectedDate.value = calendar.time
    }
}