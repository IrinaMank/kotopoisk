package com.example.zapir.kotopoisk.domain.photo

import android.net.Uri

interface AddPhotoListener {
    fun addPhotoToAdvert(photoUri: Uri)
}