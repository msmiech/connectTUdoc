package wse18.ase.qse03.mobile.ui

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.squareup.picasso.Picasso
import com.virgilsecurity.android.ethree.kotlin.interaction.EThree
import wse18.ase.qse03.mobile.R
import wse18.ase.qse03.mobile.model.User
import wse18.ase.qse03.mobile.ui.myDoctors.SignalingFirebase
import wse18.ase.qse03.mobile.model.Office
import wse18.ase.qse03.mobile.model.VirgilToken
import wse18.ase.qse03.mobile.ui.myDoctors.ChatFragment
import wse18.ase.qse03.mobile.util.RetrofitUtil
import wse18.ase.qse03.mobile.util.SettingsUtil

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var navView: NavigationView
    private lateinit var navHeader: View
    private lateinit var loginTextView: TextView
    private lateinit var logoutTextView: TextView
    private lateinit var loginImageView: ImageView
    var eThree: EThree? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.app_bar))

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.init()

        val tokenObserver = Observer<VirgilToken> { token ->
            if (token != null) {

                // Listener for register
                val registerListener =
                        object : EThree.OnCompleteListener {
                            override fun onSuccess() {
                                Log.i(TAG, "Virgil security registration successful!")
                            }

                            override fun onError(throwable: Throwable) {
                                Log.wtf(TAG, "Error during virgil security registration " + throwable.localizedMessage)
                                val toast = Toast.makeText(applicationContext, R.string.new_private_key, Toast.LENGTH_LONG)
                                toast.show()
                                eThree!!.cleanup()
                                eThree!!.register(this)
                            }
                        }

                val tokenCallback =
                        object : EThree.OnGetTokenCallback {
                            override fun onGetToken(): String {
                                return token.token
                            }
                        }

                val initializeListener =
                        object : EThree.OnResultListener<EThree> {
                            override fun onSuccess(result: EThree) {
                                // Init done!
                                eThree = result
                                Log.i(TAG, "Virgil security initialisation successful")
                                if (!eThree!!.hasLocalPrivateKey()) {
                                    eThree!!.register(registerListener)
                                }

                                // Redirect to chat fragment if called from push notification
                                val officeIdToOpen: Long = intent.getLongExtra("officeId", -1)
                                if (officeIdToOpen > 0) {
                                    viewModel.getOfficeById(officeIdToOpen)
                                }
                            }

                            override fun onError(throwable: Throwable) {
                                Log.wtf(TAG, "Error during virgil security initialisation "+throwable.localizedMessage)
                            }
                        }

                // Initialize EThree SDK with JWT token from Firebase Function
                EThree.initialize(this, tokenCallback, initializeListener)
            }
        }
        viewModel.virgilToken.observe(this, tokenObserver)

        val officeObserver = Observer<Office> { office ->
            var bundle = Bundle()
            bundle.putSerializable(ChatFragment.ARG_PARAM1, office)
            navController.navigate(R.id.chat, bundle)
        }
        viewModel.office.observe(this, officeObserver)

        initializeUI()

        // Language Test:
        // val current = resources.configuration.locale
        // Toast.makeText(this, current.toString(), Toast.LENGTH_LONG).show()
    }

    private fun initializeUI() {
        Log.e("initializeUI", "INTIALIZE")
        val sharedPreference = this.getSharedPreferences(SettingsUtil.SETTINGS_PREFERENCES_NAME, 0)
        RetrofitUtil.MEDCONNECT_BACKEND_URL = sharedPreference.getString(SettingsUtil.SETTINGS_BACKEND_URL_KEY, RetrofitUtil.MEDCONNECT_BACKEND_URL) ?: RetrofitUtil.MEDCONNECT_BACKEND_URL
        navHeader = (findViewById<NavigationView>(R.id.nav_view)).getHeaderView(0)
        loginTextView = navHeader.findViewById(R.id.login_textView)
        logoutTextView = navHeader.findViewById(R.id.logout_textView)
        loginImageView = navHeader.findViewById(R.id.login_imageView)
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            loggedIn(auth.currentUser!!)
        } else {
            loggedOut()
        }

        mDrawerLayout = findViewById(R.id.drawer_layout)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navView = findViewById(R.id.nav_view)
        navView.setCheckedItem(R.id.doctorSearch_dest)
        navView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            mDrawerLayout.closeDrawers()

            // Add code here to update the UI based on the item selected
            // For example, swap UI fragments here
            navController.navigate(menuItem.itemId)
            true
        }

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createSignInIntent() {
        val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.icon)
                        .setTheme(R.style.AppTheme)
                        .build(),
                RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {

                val user = FirebaseAuth.getInstance().currentUser!!
                loggedIn(user)

                mDrawerLayout.closeDrawers()
            } else {

                if (response == null) {
                    Toast.makeText(this, R.string.login_abort, Toast.LENGTH_SHORT).show()
                    return
                }
                Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loggedIn(user: FirebaseUser) {
        user.getIdToken(false).addOnSuccessListener(OnSuccessListener<GetTokenResult> {
            viewModel.getLoggedInUser(it.token)
            // Fetch Virgil JWT token from Firebase function
            viewModel.getVirgilToken("Bearer "+it.token)

            val sharedPref = getSharedPreferences(getString(R.string.fcm_registration_token), Context.MODE_PRIVATE)
            val registrationToken = sharedPref.getString(getString(R.string.fcm_registration_token), "")
            viewModel.subscribeToNotifications(it.token, registrationToken)
        })
        Log.e("MAINACTIVITY", "LOGIN")
        loginTextView.text = if (user.displayName.isNullOrBlank()) getString(R.string.you) else user.displayName!!
        logoutTextView.visibility = View.VISIBLE
        loginImageView.setColorFilter(Color.TRANSPARENT)
        if (user.photoUrl != null) {
            Picasso.get().load(user.photoUrl).into(loginImageView)
        } else {
            loginImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_account_circle))
            loginImageView.setColorFilter(Color.WHITE)
        }

        navHeader.setOnClickListener {
            signOut()
        }
    }

    private fun loggedOut() {
        Log.e("MAINACTIVITY", "LOGOUT")
        loginTextView.text = getString(R.string.login_title)
        logoutTextView.visibility = View.INVISIBLE
        loginImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_account_circle))
        loginImageView.setColorFilter(Color.WHITE)
        navHeader.setOnClickListener {
            createSignInIntent()
        }
        viewModel.stopListingOnIncomingCalls()
    }

    override fun onPause() {
        super.onPause()
        Log.e("MAINACTIVITY", "ONPAUSE")
        viewModel.stopListingOnIncomingCalls()
    }



    override fun onResume() {
        super.onResume()
        Log.e("MAINACTIVITY", "ONRESUME")
        //TO-CREATE Listener for incoming calls
        viewModel.user.observe(this, Observer {
            val user = it
            if (user != null) {
                viewModel.setChannel(user.id)
                viewModel.connectDatabaseReference()
                if (viewModel.getDatabaseReference() == null) {
                    Log.e(TAG, "Error connecting to database. VideoCalling not possible")
                }
            } else {
                Log.e(TAG, "User is null")
            }
            Log.e("MAIN", "Signaler null " + (viewModel.getSignaler() == null).toString())
            viewModel.getSignaler()?.setOnCallReceivedListener(object : SignalingFirebase.OnCallReceivedListener {
                override fun onCallReceived() {
                    navController.navigate(R.id.videoCallFragment_dest)
                }
            })

            viewModel.startListenOnIncomingCalls()
            Log.e("MAINACTIVITY::observer", "ONRESUME")
        })

        Log.e("MAINACTIVITY", "ONRESUME")
    }

    private fun signOut() {
        Log.e("MAINACTIVITY", "SIGNOUT")
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    loggedOut()
                    mDrawerLayout.closeDrawers()
                    navView.setCheckedItem(R.id.doctorSearch_dest)
                    navController.navigate(R.id.doctorSearch_dest)
                }
    }

    //Currently not used
    private fun delete() {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener {
                    // ...
                }
    }

    companion object {

        private const val RC_SIGN_IN = 123
        var PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        private val VERIFY_PERMISSIONS_REQUEST = 1
        val TAG = "MainActivity"
    }

    // Camera options needed, at the moment local in ChatFragment
    /**
     * verifiy all the permissions passed to the array
     * @param permissions
     */
    fun verifyPermissions(permissions: Array<String>) {
        Log.d(TAG, "verifyPermissions: verifying permissions.")

        ActivityCompat.requestPermissions(
                this@MainActivity,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        )
    }

    /**
     * Check an array of permissions
     * @param permissions
     * @return
     */
    fun checkPermissionsArray(permissions: Array<String>): Boolean {
        Log.d(TAG, "checkPermissionsArray: checking permissions array.")

        for (i in permissions.indices) {
            val check = permissions[i]
            if (!checkPermissions(check)) {
                return false
            }
        }
        return true
    }

    /**
     * Check a single permission is it has been verified
     * @param permission
     * @return
     */
    fun checkPermissions(permission: String): Boolean {
        Log.d(TAG, "checkPermissions: checking permission: $permission")

        val permissionRequest = ActivityCompat.checkSelfPermission(this@MainActivity, permission)

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermissions: \n Permission was not granted for: $permission")
            return false
        } else {
            Log.d(TAG, "checkPermissions: \n Permission was granted for: $permission")
            return true
        }
    }


}