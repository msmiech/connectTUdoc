package wse18.ase.qse03.mobile.ui.doctorSearch

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.*
import kotlinx.android.synthetic.main.doctor_search_fragment.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import wse18.ase.qse03.mobile.model.Office
import wse18.ase.qse03.mobile.adapter.OfficeAdapter
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.navigation.Navigation
import com.google.android.gms.maps.model.Marker
import java.util.*
import com.google.android.gms.maps.model.MarkerOptions
import wse18.ase.qse03.mobile.R
import wse18.ase.qse03.mobile.util.PermissionUtil
import wse18.ase.qse03.mobile.util.SettingsUtil


class DoctorSearchFragment : Fragment(), OnMapReadyCallback, LocationListener {

    companion object {
        private val TAG: String = "DoctorSearchFragment"
        fun newInstance() = DoctorSearchFragment()

        private val PERMISSON_CODE: Int = 2
        private var PERMISSIONS =
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        private var GMAP_DEFAULT_ZOOM_LEVEL = 10f
    }

    private lateinit var viewModel: DoctorSearchViewModel
    private lateinit var mapView: MapView
    private lateinit var gMap: GoogleMap
    private lateinit var listView: ListView
    private lateinit var locationManager: LocationManager
    private lateinit var sharedPreferences: SharedPreferences
    private var mapReady: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity!!.title = getString(R.string.app_name)
        val view: View = inflater.inflate(R.layout.doctor_search_fragment, container, false)

        // Google Maps init
        mapView = view.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        initSharedPreferences()

        val gpsEnabled = sharedPreferences.getBoolean(
            SettingsUtil.SETTINGS_LOCATION_GPS_ENABLED,
            SettingsUtil.SETTINGS_LOCATION_GPS_ENABLED_DEFAULT
        )
        if (gpsEnabled) {
            // Acquire a reference to the system Location Manager
            locationManager = this.context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            Log.d(DoctorSearchFragment.TAG, "Checking permissions for location...")
            if (checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "Permissions for location not granted, requesting...")
                requestPermissions(
                    PERMISSIONS,
                    DoctorSearchFragment.PERMISSON_CODE
                )
            }

            if (PermissionUtil.checkPermissions(context!!, PERMISSIONS)) {
                // Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
            } else {
                Log.d(TAG, "No permission for location.")
            }
        }


        locationManager = this.context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // List View init
        listView = view.findViewById(R.id.doctor_listView)
        listView.adapter = OfficeAdapter(context!!, ArrayList())
        listView.setOnItemClickListener { _: AdapterView<*>?, innerView: View?, position: Int, _: Long ->
            val office = ((doctor_listView.adapter as OfficeAdapter).getItem(position) as Office)
            var bundle = Bundle()
            bundle.putString(DoctorOverview.ARG_PARAM1, office.name)
            bundle.putString(DoctorOverview.ARG_PARAM2, office.address.toString())
            bundle.putString(
                DoctorOverview.ARG_PARAM3,
                office.officehours.joinToString(separator = "\n") { it.toString() })
            bundle.putString(DoctorOverview.ARG_PARAM4, office.phone)
            bundle.putLong(DoctorOverview.ARG_PARAM5, office.id)
            Navigation.findNavController(innerView!!).navigate(R.id.action_doctorSearch_dest_to_doctorOverview, bundle)

        }
        return view
    }

    private fun initSharedPreferences() {
        sharedPreferences = context!!.getSharedPreferences(SettingsUtil.SETTINGS_PREFERENCES_NAME, 0)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume() // mapView life cycle needs to be respected manually since we don't use MapsActivity
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DoctorSearchViewModel::class.java)
        // doctor_listView.adapter = OfficeAdapter(context!!, ArrayList<Office>())
        viewModel.init()
        val officeListObserver = Observer<List<Office>> { offices ->
            Log.d("LOG_TAG", "Office list updated. Number of offices: " + offices.size)
            if (offices != null) {
                // Update UI.
                (doctor_listView.adapter as OfficeAdapter).updateData(offices)
                gMap.clear()
                for (office: Office in offices) {
                    //var marker = Marker()
                    var currentLatLong = getLocationFromAddress(office.address.toSearchAddressString())
                    var marker: Marker = gMap.addMarker(MarkerOptions().position(currentLatLong).title(office.name))
                    marker.tag = office.id

                    //gMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLong));

                }
            }

        }
        viewModel.officeListLD.observe(this, officeListObserver)
        val searchBar: SearchView? = view?.findViewById(R.id.doctor_searchView)
        searchBar?.isFocusable = false
        searchBar?.isIconified = false
        searchBar?.clearFocus()
        searchBar?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("DoctorSearch", "SearchText changed to: " + newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("DoctorSearch", "SearchText submitted: " + query)
                viewModel.updateOfficeList(query)
                searchBar.clearFocus()
                return true
            }

        })
    }

    /**
     * ---------------------------Google Maps---------------------------------------
     *
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(map: GoogleMap?) {
        Log.d(TAG, "onMapReady called...")
        if (map == null) {
            Log.w(TAG, "OnMapReadyCallback delivered null map")
            return
        }
        gMap = map
        var selectedCenter = getLocationFromAddress(
            sharedPreferences.getString(
                SettingsUtil.SETTINGS_LOCATION_KEY,
                SettingsUtil.SETTINGS_LOCATION_DEFAULT
            )!!
        )
        if (selectedCenter.latitude == 0.0 && selectedCenter.longitude == 0.0) {
            selectedCenter = LatLng(48.210033, 16.363449)
            val toast = Toast.makeText(context, R.string.settings_location_doeas_not_exist_msg, Toast.LENGTH_LONG)
            toast.show()
        }
        val center = CameraUpdateFactory.newLatLng(
            selectedCenter
        )
        val zoom = CameraUpdateFactory.zoomTo(GMAP_DEFAULT_ZOOM_LEVEL)

        gMap.moveCamera(center)
        gMap.animateCamera(zoom)

        gMap.setOnInfoWindowClickListener { marker: Marker ->
            val officeId = marker.tag as Long
            val position: Int = (listView.adapter as OfficeAdapter).getItemPosition(officeId)
            listView.performItemClick(view, position, officeId)
        }

        // check again whether GPS setting is enabled
        val gpsEnabled = sharedPreferences.getBoolean(
            SettingsUtil.SETTINGS_LOCATION_GPS_ENABLED,
            SettingsUtil.SETTINGS_LOCATION_GPS_ENABLED_DEFAULT
        )
        if (gpsEnabled && checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // show my location on map
            gMap.isMyLocationEnabled = true
        }

        mapReady = true
    }

    fun getLocationFromAddress(addressToConvert: String): LatLng {

        if (!Geocoder.isPresent()) {
            Log.d(TAG, "Geocoder not present")
            return LatLng(0.0, 0.0)
        }
        val coder = Geocoder(this.context, Locale.getDefault())
        val address: List<Address>?
        var resLatLng: LatLng?

        // May throw an IOException
        address = coder.getFromLocationName(addressToConvert, 5)
        if (address == null) {
            return LatLng(0.0, 0.0)
        }

        if (address.isEmpty()) {
            return LatLng(0.0, 0.0)
        }

        val location = address[0]

        resLatLng = LatLng(location.latitude, location.longitude)

        return resLatLng
    }


    // -------------- LocationListener impl -----------------
    override fun onLocationChanged(location: Location?) {
        if (mapReady && location != null) {
            val latLng = LatLng(location.latitude, location.longitude)
            Log.i(TAG, "LocationListener::onLocationChanged: $latLng")
            val camUpdate = CameraUpdateFactory.newLatLng(
                latLng
            )
            val currentZoomLevel = gMap.cameraPosition.zoom
            gMap.moveCamera(camUpdate)

            val zoom = CameraUpdateFactory.zoomTo(currentZoomLevel) // (GMAP_DEFAULT_ZOOM_LEVEL)
            gMap.animateCamera(zoom)
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.i(TAG, "LocationListener::onStatusChanged: $provider $status")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.i(TAG, "LocationListener::onProviderEnabled: $provider")
    }

    override fun onProviderDisabled(provider: String?) {
        Log.i(TAG, "LocationListener::onProviderDisabled: $provider")
    }
}
