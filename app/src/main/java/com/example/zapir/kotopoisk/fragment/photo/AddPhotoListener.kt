package com.example.zapir.kotopoisk.fragment.photo

import android.net.Uri

interface AddPhotoListener {
    fun addPhotoToAdvert(photoUri: Uri)
}