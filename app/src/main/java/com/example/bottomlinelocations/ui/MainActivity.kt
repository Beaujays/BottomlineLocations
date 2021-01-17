package com.example.bottomlinelocations.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bottomlinelocations.R
import com.example.bottomlinelocations.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.FileInputStream
import java.io.InputStream
import java.util.*
import kotlin.system.exitProcess

private const val RC_SIGN_IN = 1234

@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {
    private lateinit var locale: Locale
    private var currentLanguage = ""
    private var currentLang: String? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var storageReference: StorageReference
    private lateinit var locationManager: LocationManager
    private lateinit var lastLocation: Location
    private val locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (FirebaseAuth.getInstance().currentUser == null) {
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build()
            )

            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(), RC_SIGN_IN
            )
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_addSiteDefect,
                R.id.nav_listSiteDefects
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                recreate()
            } else {
                finishAffinity()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
        exitProcess(0)
    }

    //region Set language
    fun setLocale(localeName: String) {
        if (localeName != currentLanguage) {
            locale = Locale(localeName)
            val res = this.resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(
                this,
                MainActivity::class.java
            )
            refresh.putExtra(currentLang, localeName)
            startActivity(refresh)
        } else {
            Toast.makeText(
                this@MainActivity, "Language, , already, , selected)!", Toast.LENGTH_SHORT
            ).show()
        }
    }
    //endregion

    //region Upload image to FireStore
    private fun addUploadRecordToDb(uri: String) {
        val db = FirebaseFirestore.getInstance()

        val data = HashMap<String, Any>()
        data["imageUrl"] = uri

        db.collection("posts")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Saved to DB", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error saving to DB", Toast.LENGTH_LONG).show()
            }
    }
    //endregion

    //region Upload image to storage
    fun uploadImage(filePath: String, picId: Int) {
        storageReference = FirebaseStorage.getInstance().reference
        // Set storage reference
        val ref = storageReference.child("uploads/sitedefect$picId")
        val stream: InputStream = FileInputStream(filePath) // Set file from filePath in file input
        val uploadTask = ref.putStream(stream) // Take stream for uploadTask

        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                addUploadRecordToDb(picId.toString())
                Log.i("MainActivity", "Uri is $picId")
            } else {
                // Handle failures
                Log.i("MainActivity", "Upload failed in on complete")
            }
        }.addOnFailureListener {
            Log.i("MainActivity", "Upload failed in on failure")
        }
    }
    //endregion

    //region Get location
    fun getLocation(provider: String): Location {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
        // Get last known location
        lastLocation = locationManager.getLastKnownLocation(provider)

        // Check if location has changed
        locationManager.requestLocationUpdates(
            provider,
            0L,
            0f,
            locationListener
        )

        return lastLocation
    }
    //endregion

    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // Location listener set last location with new location if new
            lastLocation.set(location)
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    //region Check permissions
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //endregion

    //region Convert geoLocation to addresses
    fun getCompleteAddressString(LONGITUDE: Double, LATITUDE: Double, handler: Handler): String? {
        val geoCoder = Geocoder(applicationContext, Locale.getDefault())
        var result: String = null.toString()
        try {
            val addresses: List<Address> =
                // Get location address from lat and long
                geoCoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if ((addresses.isNotEmpty())) {
                // Set found address in array
                val address = addresses[0]
                val sb = StringBuilder()
                for (i in 0 until address.maxAddressLineIndex) sb.append(address.getAddressLine(i))
                    .append(",\n")
                // Set values and split with ","
                sb.append(address.locality).append(",")
                sb.append(address.postalCode).append(",")
                sb.append(address.thoroughfare).append(" ")
                sb.append(address.featureName).append(",")
                sb.append(address.countryName)
                result = sb.toString()
            }
            Log.i("", result)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Can't get address", Toast.LENGTH_LONG).show()
        } finally {
            // Set result in handler
            val message = Message.obtain()
            message.target = handler
            message.what = 1
            val bundle = Bundle()
            result = (result)
            bundle.putString("address", result)
            message.data = bundle
            message.sendToTarget()
        }
        return result
    }
    //endregion
}
