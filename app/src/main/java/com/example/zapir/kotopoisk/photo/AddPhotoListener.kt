package com.example.zapir.kotopoisk.photo

import android.net.Uri

interface AddPhotoListener {
    fun addPhotoToAdvert(photoUri: Uri)
}