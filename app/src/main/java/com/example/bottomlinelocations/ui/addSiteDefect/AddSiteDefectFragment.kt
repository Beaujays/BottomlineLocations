package com.example.bottomlinelocations.ui.addSiteDefect

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.location.LocationManager
import android.os.*
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bottomlinelocations.MyApplication
import com.example.bottomlinelocations.R
import com.example.bottomlinelocations.data.SiteDefects
import com.example.bottomlinelocations.databinding.FragmentAddSiteDefectsBinding
import com.example.bottomlinelocations.ui.MainActivity
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@Suppress("DEPRECATION")
class AddSiteDefectFragment : Fragment() {

    private val addSiteDefectViewModel: AddSiteDefectViewModel by viewModels {
        val application = requireActivity().application as MyApplication
        val siteDefectRepository = application.siteDefectRepository
        AddSiteDefectViewModelFactory(siteDefectRepository)
    }

    // Set late init and variables
    private lateinit var binding: FragmentAddSiteDefectsBinding
    private lateinit var name: EditText
    private lateinit var address: EditText
    private lateinit var zipcode: EditText
    private lateinit var city: EditText
    private lateinit var remark: EditText
    private lateinit var image: ImageView
    private var picId: Int = -1

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_site_defects,
            container,
            false
        )
        // Binding layout field with variable value
        name = binding.editTextName
        address = binding.editTextAddress
        zipcode = binding.editTextZipcode
        city = binding.editTextCity
        remark = binding.editTextDescription
        image = binding.clickImage

        //region Button make a photo
        binding.buttonMakePhoto.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Start the activity with camera_intent,
            // and request pic id
            picId = Random.nextInt(10000)
            // Start the activity with camera_intent,
            // and request pic id
            startActivityForResult(cameraIntent, picId)
        }
        //endregion

        var latitude: Double
        var longitude: Double
        //region Get live location and convert to address
        binding.getLocation.setOnClickListener {
            val lastLocation = (activity as MainActivity).getLocation(LocationManager.GPS_PROVIDER)
            latitude = lastLocation.latitude
            longitude = lastLocation.longitude
            (activity as MainActivity).getCompleteAddressString(longitude,latitude,GeoCodeHandler())
        }
        //endregion

        //region Save site defect
        // Button for save the site defect
        binding.buttonSaveSiteDefect.setOnClickListener {
            if (TextUtils.isEmpty(name.text)) {
                val toast = Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT)
                toast.show()
            } else {
                // Send new site defect with email
                val intent = ShareCompat.IntentBuilder
                    .from(requireActivity())
                    .setType("text/plain")
                    .setSubject("New site defect ${name.text} ${address.text}")
                    .setText(
                        "Hello planning, \n\n" +
                                "Please find below the address and remarks from the driver. \n" +
                                " Name: ${name.text}\n" +
                                "Address: ${address.text}\n" +
                                "City: ${city.text}\n" +
                                "Remarks: " +
                                "${remark.text}\n\n" +
                                "Kind regards."
                    )
                    .intent
                startActivity(intent)

                // Save site defect in database
                Log.i("buttonSave", "Not empty filled with ${name.text}")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val siteDefects = SiteDefects(
                        "",
                        name.text.toString(),
                        address.text.toString(),
                        zipcode.text.toString(),
                        city.text.toString(),
                        remark.text.toString(),
                        DateTimeFormatter
                            .ofPattern("yyyy-MM-dd HH:mm")
                            .withZone(ZoneId.of("Europe/Amsterdam"))
                            .format(Instant.now()),
                        picId.toString()
                    )
                    // Add in Fire store
                    addSiteDefectViewModel.insert(siteDefects)
                }
                Toast.makeText(context,"Added site defect", Toast.LENGTH_LONG).show()
            }
            findNavController().navigate(R.id.action_nav_addSiteDefect_to_nav_listSiteDefects)
        }
        //endregion

        return binding.root
    }

    //region Upload image to Storage
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Match the request 'pic id with requestCode
        if (requestCode == picId) {

            // BitMap is data structure of image file
            // which stores the image in memory
            val photo: Bitmap = data?.extras
                ?.get("data") as Bitmap
            image.setImageBitmap(photo)

            // Set file in temporary external storage
            val f3 = File(Environment.getExternalStorageDirectory().toString() + "/bottomline")
            if (!f3.exists()) f3.mkdirs()
            val outStream: OutputStream?
            val file = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/siteDefect_$picId" + ".png"
            )
            try {
                outStream = FileOutputStream(file) // Set file from path in file output
                photo.compress(Bitmap.CompressFormat.PNG, 85, outStream) // Compress photo
                (activity as MainActivity).uploadImage(file.toString(), picId)
                outStream.close()
                Toast.makeText(context, "Saved $file", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    //endregion

    //region Get address info and set in text
    @SuppressLint("HandlerLeak")
    internal inner class GeoCodeHandler : Handler() {
        override fun handleMessage(message: Message) {
            val locationAddress: String
            locationAddress = when (message.what) {
                1 -> {
                    val bundle = message.data
                    bundle.getString("address").toString() // Get bundle with key
                }
                else -> null.toString()
            }
            // Set text value with outcome handler
            val lstValues: List<String> = locationAddress.split(",").map { it.trim() }
            repeat(lstValues.size) {
                city.setText(lstValues[0])
                zipcode.setText(lstValues[1])
                address.setText(lstValues[2])
            }
        }
    }
    //endregion
}

