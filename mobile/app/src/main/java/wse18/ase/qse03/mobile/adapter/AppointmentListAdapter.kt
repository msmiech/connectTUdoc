package wse18.ase.qse03.mobile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import wse18.ase.qse03.mobile.R
import wse18.ase.qse03.mobile.model.Appointment
import kotlinx.android.synthetic.main.appointment_list_item.view.*
import kotlinx.android.synthetic.main.appointment_list_item_extended.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AppointmentListAdapter(
    val context: Context,
    var dataSource: MutableList<Appointment>,
    var listParentType: String
) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val APPOINTMENT_OVERVIEW = "appointment-overview"
    private val PERSONAL_APPOINTMENTS = "personal_appointments"
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val appointment = getItem(position) as Appointment
        var dateFormatterTime = SimpleDateFormat("HH:mm")
        if (listParentType.equals(PERSONAL_APPOINTMENTS)) {
            var item_text = appointment.office.name + ": "
            val dateFormatterDate = SimpleDateFormat("dd.MM.yyyy")
            val rowView = inflater.inflate(R.layout.appointment_list_item_extended, parent, false)
            rowView.appointment_office_name.text = appointment.office.name + ":"
            rowView.appointment_time.text = dateFormatterDate.format(appointment.appointmentBegin) + ":  " + dateFormatterTime.format(appointment.appointmentBegin) + " - " + dateFormatterTime.format(appointment.appointmentEnd)
            return rowView
        }
        val rowView = inflater.inflate(R.layout.appointment_list_item, parent, false)
        rowView.appointment_begin.text =dateFormatterTime.format(appointment.appointmentBegin)
        rowView.appointment_end.text = dateFormatterTime.format(appointment.appointmentEnd)
        return rowView
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        //return dataSource[position].id
        //Generated appointments do not have a ID because they are not persisted yet
        return 0
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    fun updateData(list: List<Appointment>) {
        dataSource.clear()
        dataSource.addAll(list)
        this.notifyDataSetChanged()
    }


}