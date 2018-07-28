package com.example.zapir.kotopoisk.photo

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.widget.Toast
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import com.example.zapir.kotopoisk.activity.MainActivity

class PhotoHandler(private var context: MainActivity) {

    companion object {
        const val CAMERA_CAPTURE = 1
        const val OPEN_GALLERY = 2
    }

    fun openGallery() {
        context.startActivityForResult(Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI), OPEN_GALLERY)
    }

    fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(context.packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                showToast(context, "There was a problem saving the photo...")
            }
            if (photoFile != null) {
                context.photoURI = FileProvider.getUriForFile(context,
                        "com.noveogroup.ru.android.fileprovider",
                        photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, context.photoURI)
                context.startActivityForResult(intent, CAMERA_CAPTURE)
            }
        }
    }

    private fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        context.currentPhotoPath = image.absolutePath
        return image
    }

}