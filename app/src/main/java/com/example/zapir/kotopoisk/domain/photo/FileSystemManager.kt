package com.example.zapir.kotopoisk.domain.photo

import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import android.provider.MediaStore
import com.example.zapir.kotopoisk.ui.base.BaseActivity
import android.graphics.BitmapFactory
import java.io.IOException


class FileSystemManager(private var context: BaseActivity) {

    private val DEGREES_180 = 180f
    private val DEGREES_90 = 90f
    private val KBlimit = 10240000

    fun decodeImageFromUri(uri: Uri): Bitmap? {
        val bitmap = decodeBitmapFromUri(uri)
        if (bitmap != null) {
            val realPath = getRealPathFromCameraUri(uri)
            return if (realPath == null) bitmap else fixCameraOrientation(bitmap, realPath)
        } else {
            return bitmap
        }
    }

    private fun decodeBitmapFromUri(uri: Uri): Bitmap? {
        try {
            var imageStream = context.contentResolver.openInputStream(uri)
            if (imageStream == null) {
                return null
            } else {
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeStream(imageStream, null, options)
                options.inSampleSize = calculateInSampleSize(options)
                options.inJustDecodeBounds = false
                imageStream.close()
                imageStream = context.contentResolver.openInputStream(uri)
                if (imageStream == null) {
                    return null
                } else {
                    val bitmap = BitmapFactory.decodeStream(imageStream, null, options)
                    imageStream.close()
                    return bitmap
                }
            }
        } catch (e: IOException) {
            Log.e("FileSystemManager error", e.message)
        }

        return null
    }

    private fun getRealPathFromCameraUri(uri: Uri): String? {
        var realPath: String? = null
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DATA),
                    null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                realPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
        } catch (e: Exception) {
            Log.e(e.message, e.toString())
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        return realPath
    }

    private fun fixCameraOrientation(cameraBitmap: Bitmap, path: String): Bitmap {
        val matrix = Matrix()
        when (getExifOrientation(path)) {
            ExifInterface.ORIENTATION_NORMAL -> return cameraBitmap
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(DEGREES_180)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(DEGREES_180)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(DEGREES_90)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(DEGREES_90)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-DEGREES_90)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-DEGREES_90)
            else -> return cameraBitmap
        }
        try {
            val rotated = Bitmap.createBitmap(cameraBitmap, 0, 0, cameraBitmap.width,
                    cameraBitmap.height, matrix, true)
            cameraBitmap.recycle()
            return rotated
        } catch (e: OutOfMemoryError) {
            Log.e(e.message, e.toString())
        }

        return cameraBitmap
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        val weight = height * width * 4
        val quality = (100*KBlimit)/(weight*2)
        val sqaleFactor = quality * 2f /100
        val reqHeight = (height*sqaleFactor).toInt()
        val reqWidth = (width*sqaleFactor).toInt()

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    private fun getExifOrientation(path: String): Int {
        return try{
            val exifOrientation = ExifInterface(path)
            exifOrientation.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL)
        } catch (e: IOException){
            ExifInterface.ORIENTATION_NORMAL
        }
    }

}