package com.ch2ps075.talenthubmitra.ui.register

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresExtension
import com.ch2ps075.talenthubmitra.R
import com.ch2ps075.talenthubmitra.databinding.ActivityVerifyPhotoBinding
import com.ch2ps075.talenthubmitra.helper.reduceFileImage
import com.ch2ps075.talenthubmitra.helper.uriToFile
import com.ch2ps075.talenthubmitra.state.ResultState
import com.ch2ps075.talenthubmitra.ui.ViewModelFactory
import com.ch2ps075.talenthubmitra.ui.main.MainActivity

class VerifyPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyPhotoBinding
    private val viewModel by viewModels<RegisterViewModel> { ViewModelFactory.getInstance(this) }
    private var currentImageUri: Uri? = null

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.uploadPhoto.setOnClickListener { startGalleryOrOpenDocument() }
        binding.nextButton.setOnClickListener { verifyTalentPhoto() }
    }

    private fun verifyTalentPhoto() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()

            viewModel.prediction(imageFile).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {

                        }

                        is ResultState.Success -> {
                            AlertDialog.Builder(this).apply {
                                setTitle("Hasil prediksi")
                                setMessage(result.data.data.soilTypesPrediction)
                                setPositiveButton("Lanjut") { _, _ ->
                                    startActivity(
                                        Intent(
                                            this@VerifyPhotoActivity,
                                            MainActivity::class.java
                                        )
                                    )
                                    finish()
                                }
                                create()
                                show()
                            }
                        }

                        is ResultState.Error -> {
                            showToast("Gagal", Toast.LENGTH_LONG)
                        }

                    }
                }
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

    private fun showImage() {
        currentImageUri?.let { uri ->
            binding.previewImageView.setImageURI(uri)
        }
    }

    private fun showToast(message: String, length: Int) {
        Toast.makeText(this, message, length).show()
    }
}