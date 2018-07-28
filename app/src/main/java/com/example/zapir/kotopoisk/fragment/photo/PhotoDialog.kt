package com.example.zapir.kotopoisk.fragment.photo

import android.Manifest
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.activity.MainActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PhotoDialog : DialogFragment() {

    companion object {
        const val CAMERA_CAPTURE = 1
        const val OPEN_GALLERY = 2
    }

    private val items: Array<String> by lazy {
        arrayOf(getString(R.string.dialog1), getString(R.string.dialog2))
    }
    private val mainContext by lazy {
        activity as? MainActivity
                ?: throw Exception("PhotoDialog has no mainContext")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(mainContext)
        builder.setTitle(R.string.dialog_title)
                .setItems(items, { dialog, which ->
                    when (which) {
                        0 -> openGallery()
                        1 -> openCamera()
                    }
                })
        return builder.create()
    }

    private fun openGallery() {
        Log.d("start Intent", "MediaStore.ACTION_PICK")
        mainContext.startActivityForResult(Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI), OPEN_GALLERY)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(mainContext.packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                showToast(mainContext, "There was a problem saving the photo...")
            }
            if (photoFile != null) {
                mainContext.photoURI = FileProvider.getUriForFile(mainContext,
                        "com.noveogroup.ru.android.fileprovider",
                        photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mainContext.photoURI)
                mainContext.startActivityForResult(intent, CAMERA_CAPTURE)
            }
        }
    }

    private fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = mainContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mainContext.currentPhotoPath = image.absolutePath
        return image
    }
}
