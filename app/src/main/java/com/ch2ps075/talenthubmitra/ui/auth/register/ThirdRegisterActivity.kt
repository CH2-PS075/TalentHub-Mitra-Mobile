package com.ch2ps075.talenthubmitra.ui.auth.register

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresExtension
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ch2ps075.talenthubmitra.R
import com.ch2ps075.talenthubmitra.databinding.ActivityThirdRegisterBinding
import com.ch2ps075.talenthubmitra.helper.reduceFileImage
import com.ch2ps075.talenthubmitra.helper.uriToFile
import com.ch2ps075.talenthubmitra.state.ResultState
import com.ch2ps075.talenthubmitra.ui.ViewModelFactory
import com.ch2ps075.talenthubmitra.ui.auth.login.LoginActivity
import com.ch2ps075.talenthubmitra.ui.auth.register.FirstRegisterActivity.Companion.TALENT_NAME
import com.ch2ps075.talenthubmitra.ui.auth.register.FirstRegisterActivity.Companion.ADDRESS
import com.ch2ps075.talenthubmitra.ui.auth.register.FirstRegisterActivity.Companion.CONTACT
import com.ch2ps075.talenthubmitra.ui.auth.register.FirstRegisterActivity.Companion.PRICE
import com.ch2ps075.talenthubmitra.ui.auth.register.FirstRegisterActivity.Companion.CATEGORY
import com.ch2ps075.talenthubmitra.ui.auth.register.FirstRegisterActivity.Companion.GROUP_TYPE
import com.ch2ps075.talenthubmitra.ui.auth.register.FirstRegisterActivity.Companion.EMAIL
import com.ch2ps075.talenthubmitra.ui.auth.register.FirstRegisterActivity.Companion.PASSWORD
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class ThirdRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> { ViewModelFactory.getInstance(this) }
    private var currentImageUri: Uri? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cbLocation.setOnClickListener {
            if (!checkPermission(FINE_LOCATION_REQUIRED_PERMISSION) &&
                !checkPermission(COARSE_LOCATION_REQUIRED_PERMISSION)
            ) {
                requestPermissionLauncher.launch(
                    arrayOf(
                        FINE_LOCATION_REQUIRED_PERMISSION,
                        COARSE_LOCATION_REQUIRED_PERMISSION
                    )
                )
            }
        }

        with(binding) {
            selectImageButton.setOnClickListener { startGalleryOrOpenDocument() }
            finalRegisterButton.setOnClickListener {
                when {
                    edRegisterPortfolio.text.toString().isEmpty() -> {
                        edRegisterPortfolio.error = getString(R.string.error_empty_field)
                    }

                    edRegisterDescription.text.toString().isEmpty() -> {
                        edRegisterDescription.error = getString(R.string.error_empty_field)
                    }

                    else -> if (binding.cbLocation.isChecked) {
                        getMyLastLocation { latitude, longitude ->
                            register(latitude, longitude)
                        }
                    } else {
                        register()
                    }

                }
            }
        }
    }

    private fun register(latitude: Double? = null, longitude: Double? = null) {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val getBundle = intent.extras
            val talentName = getBundle?.getString(TALENT_NAME)
            val address = getBundle?.getString(ADDRESS)
            val contact = getBundle?.getString(CONTACT)
            val price = getBundle?.getString(PRICE)
            val category = getBundle?.getString(CATEGORY)
            val groupType = getBundle?.getString(GROUP_TYPE)
            val email = getBundle?.getString(EMAIL)
            val password = getBundle?.getString(PASSWORD)
            val portfolio = binding.edRegisterPortfolio.text.toString()
            val description = binding.edRegisterDescription.text.toString()

            if (talentName != null && address != null && contact != null && price != null && category != null && groupType != null && email != null && password != null) {
                viewModel.register(
                    talentName,
                    category,
                    groupType,
                    address,
                    contact,
                    price,
                    email,
                    password,
                    description,
                    portfolio,
                    latitude,
                    longitude,
                    imageFile
                ).observe(this) { result ->

                    if (result != null) {
                        when (result) {
                            is ResultState.Loading -> {
                                showLoading(true)
                            }

                            is ResultState.Success -> {
                                showSuccessAlert()
                            }

                            is ResultState.Error -> {
                                showErrorAlert()
                                showLoading(false)
                            }
                        }
                    }

                }
            } else {
                showToast("Ada input yang kosong", Toast.LENGTH_SHORT)
            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    private fun startGalleryOrOpenDocument() {
        if (isPhotoPickerAvailable()) {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            showToast(getString(R.string.photo_picker_not_available), Toast.LENGTH_LONG)
            openDocumentPicker()
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    private fun isPhotoPickerAvailable(): Boolean {
        val photoPickerIntent = Intent(MediaStore.ACTION_PICK_IMAGES)
        val packageManager = packageManager
        return photoPickerIntent.resolveActivity(packageManager) != null
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            showToast(getString(R.string.photo_picker), Toast.LENGTH_SHORT)
        }
    }

    private fun openDocumentPicker() {
        val openDocumentIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        openDocumentIntent.type = "image/*"
        openDocumentIntent.addCategory(Intent.CATEGORY_OPENABLE)
        launcherOpenDocument.launch(openDocumentIntent)
    }

    private val launcherOpenDocument = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri = data?.data
            if (uri != null) {
                currentImageUri = uri
                showImage()
            } else {
                showToast(getString(R.string.open_document), Toast.LENGTH_SHORT)
            }
        }
    }

    private fun showSuccessAlert() {
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText(getString(R.string.success_title))
            .setContentText(getString(R.string.success_register_talent))
            .setConfirmButton(getString(R.string.continue_title)) {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            .apply { setCanceledOnTouchOutside(false) }
            .show()
    }

    private fun showErrorAlert() {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText(getString(R.string.failed_title))
            .setContentText(getString(R.string.invalid_picture_error))
            .setConfirmButton(getString(R.string.try_again_title)) {
                it.dismissWithAnimation()
            }
            .apply { setCanceledOnTouchOutside(false) }
            .show()
    }

    private fun showImage() {
        currentImageUri?.let { uri ->
            binding.previewImageView.setImageURI(uri)
        }
    }

    private fun showToast(message: String, length: Int) {
        Toast.makeText(this, message, length).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getMyLastLocation(latLng: (Double?, Double?) -> Unit = { _, _ -> }) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (checkPermission(FINE_LOCATION_REQUIRED_PERMISSION) &&
            checkPermission(COARSE_LOCATION_REQUIRED_PERMISSION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    latLng(latitude, longitude)
                } else {
                    showToast(getString(R.string.location_not_found), Toast.LENGTH_SHORT)
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    FINE_LOCATION_REQUIRED_PERMISSION,
                    COARSE_LOCATION_REQUIRED_PERMISSION
                )
            )
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
            }
        }

    companion object {
        private const val FINE_LOCATION_REQUIRED_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val COARSE_LOCATION_REQUIRED_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
    }
}