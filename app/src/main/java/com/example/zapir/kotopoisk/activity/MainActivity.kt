package com.example.zapir.kotopoisk.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.example.zapir.kotopoisk.BottomNavigationViewHelper
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.fragment.photo.AddPhotoListener
import com.example.zapir.kotopoisk.fragment.photo.AddingPhoto
import com.example.zapir.kotopoisk.fragment.photo.PhotoDialog
import com.example.zapir.kotopoisk.fragment.photo.TicketActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), AddPhotoListener {

    lateinit var currentPhotoPath: String
    lateinit var photoURI: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialization BottomNavigationViewHelper
        BottomNavigationViewHelper.create(
                supportActionBar,
                navigation_view,
                pager,
                supportFragmentManager
        )
    }

    override fun addPhotoToAdvert(photoUri: Uri){
        Log.d("asking instance of", "AddingPhoto")
//        supportFragmentManager.beginTransaction()
//                .replace(R.id.container, AddingPhoto.newInstance(photoUri))
//                .addToBackStack(AddingPhoto.TAG)
//                .commit()
        startActivity(TicketActivity.newIntent(this, photoUri))

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PhotoDialog.CAMERA_CAPTURE && resultCode == RESULT_OK) {
            Log.d("received Intent", "CAMERA_CAPTURE")
            addPhotoToAdvert(photoURI)
        } else if (requestCode == PhotoDialog.OPEN_GALLERY && resultCode == RESULT_OK) {
            Log.d("received Intent", "OPEN_GALLERY")

            val imageUri = data?.data ?: throw Exception("empty data in OPEN_GALLERY")
            addPhotoToAdvert(imageUri)
        }
    }

}