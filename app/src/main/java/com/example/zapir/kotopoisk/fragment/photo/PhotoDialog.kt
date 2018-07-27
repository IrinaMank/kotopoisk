package com.example.zapir.kotopoisk.fragment.photo

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.activity.MainActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PhotoDialog: DialogFragment() {

    companion object {
        const val CAMERA_CAPTURE = 1
        const val OPEN_GALLERY = 2
    }

    private val items: Array<String> by lazy {
        arrayOf(getString(R.string.dialog1), getString(R.string.dialog2))
    }
    private val mainContext: Context by lazy { getActivity() as? MainActivity
            ?: throw Exception("PhotoDialog has no mainContext") }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(mainContext)
        builder.setTitle(R.string.dialog_title)
                .setItems(items, { dialog, which ->
                    when(which){
                        0 -> openGallery()
                        1 -> openCamera()
                    }
                })
        return builder.create()
    }

    private fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun openGallery(){
        Log.d("start Intent", "OPEN_GALLERY")
        activity?.startActivityForResult(Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI), OPEN_GALLERY)
        ?: throw Exception("no activity")
    }

    private fun openCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val mainActivity = activity as? MainActivity
                ?: throw Exception("PhotoDialog has no mainContext")
        if (intent.resolveActivity(mainContext.packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                showToast(mainContext, "There was a problem saving the photo...")
            }
            if (photoFile != null) {
                val fileUri = Uri.fromFile(photoFile)
                mainActivity.photoURI = fileUri
                mainActivity.currentPhotoPath = fileUri.getPath()
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                Log.d("start Intent", "CAMERA_CAPTURE")
                activity?.startActivityForResult(intent, CAMERA_CAPTURE)
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = mainContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
    }

}
