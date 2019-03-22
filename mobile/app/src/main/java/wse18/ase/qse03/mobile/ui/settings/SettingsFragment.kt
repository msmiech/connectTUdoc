package wse18.ase.qse03.mobile.ui.settings


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.settings_fragment.*

import wse18.ase.qse03.mobile.R
import wse18.ase.qse03.mobile.util.RetrofitUtil
import wse18.ase.qse03.mobile.util.SettingsUtil

/**
 * A simple [Fragment] subclass.
 *
 */
class SettingsFragment : Fragment() {

    private val DEFAULT_LOCATION: String = "Wien"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity!!.title = getString(R.string.settings)
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.settings_fragment, container, false)

        // init sharedPreferences
        val sharedPreferences = context!!.getSharedPreferences(SettingsUtil.SETTINGS_PREFERENCES_NAME, 0)

        val location =
            sharedPreferences.getString(SettingsUtil.SETTINGS_LOCATION_KEY, SettingsUtil.SETTINGS_LOCATION_DEFAULT)
                ?: DEFAULT_LOCATION
        val locationInput = view.findViewById<TextInputEditText>(R.id.location_input)
        locationInput.setText(location)

        val gpsEnabled = sharedPreferences.getBoolean(
            SettingsUtil.SETTINGS_LOCATION_GPS_ENABLED,
            SettingsUtil.SETTINGS_LOCATION_GPS_ENABLED_DEFAULT
        )
        val gpsEnabledSwitch = view.findViewById<Switch>(R.id.gps_input)
        gpsEnabledSwitch.isChecked = gpsEnabled

        val backendUrlInput = view.findViewById<TextInputEditText>(R.id.backend_url_input)
        backendUrlInput.setText(RetrofitUtil.MEDCONNECT_BACKEND_URL)


        val saveSettingsBtn = view.findViewById<Button>(R.id.button_save_settings)
        saveSettingsBtn?.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putString(SettingsUtil.SETTINGS_LOCATION_KEY, locationInput.text.toString())
            RetrofitUtil.MEDCONNECT_BACKEND_URL = backendUrlInput.text.toString()
            editor.putString(SettingsUtil.SETTINGS_BACKEND_URL_KEY, backendUrlInput.text.toString())
            editor.putBoolean(SettingsUtil.SETTINGS_LOCATION_GPS_ENABLED, gpsEnabledSwitch.isChecked)
            editor.apply()

            val toast = Toast.makeText(context, R.string.settings_saved_msg, Toast.LENGTH_LONG)
            toast.show()
        }
        return view
    }

}
