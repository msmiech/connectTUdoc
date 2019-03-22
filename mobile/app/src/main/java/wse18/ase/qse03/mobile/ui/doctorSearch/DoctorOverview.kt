package wse18.ase.qse03.mobile.ui.doctorSearch

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import wse18.ase.qse03.mobile.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DoctorOverview.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DoctorOverview.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DoctorOverview : Fragment() {
    private var id : Long? = null
    private var name: String? = null
    private var address: String? = null
    private var telephone: String? = null
    private var officehours: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getLong(ARG_PARAM5)
            name = it.getString(ARG_PARAM1)
            address = it.getString(ARG_PARAM2)
            officehours = it.getString(ARG_PARAM3)
            telephone = it.getString(ARG_PARAM4)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.doctor_overview_fragment, container, false)
        val nameView: TextView = view.findViewById(R.id.display_name)
        nameView.text = name
        val telephoneView: TextView = view.findViewById(R.id.display_telephone)
        telephoneView.text = telephone
        val addressView: TextView = view.findViewById(R.id.display_address)
        addressView.text = address
        val officehoursView: TextView = view.findViewById(R.id.display_officehours)
        officehoursView.text = officehours
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appointmentButton : Button = view.findViewById(R.id.button_available_appointments)
        appointmentButton.setOnClickListener {
            var bundle = Bundle()
            bundle.putLong("officeId", id!!)
            Navigation.findNavController(view).navigate(R.id.action_doctorOverview_to_appointmentOverviewFragment, bundle)
        }
    }


    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        const val ARG_PARAM1 = "name"
        const val ARG_PARAM2 = "address"
        const val ARG_PARAM3 = "officehours"
        const val ARG_PARAM4 = "telephone"
        const val ARG_PARAM5 = "id"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DoctorOverview.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String, param3: String, param4: String, param5: Long) =
            DoctorOverview().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                    putString(ARG_PARAM4, param4)
                    putLong(ARG_PARAM5, param5)
                }
            }
    }
}
