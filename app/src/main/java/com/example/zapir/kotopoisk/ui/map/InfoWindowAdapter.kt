package com.example.zapir.kotopoisk.ui.map

import android.app.Activity
import android.content.Context
import android.view.View
import com.example.zapir.kotopoisk.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class InfoWindowAdapter(private val context: Context?) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker?): View? {
        return null
    }

    override fun getInfoContents(marker: Marker?): View? {
        return (context as Activity)
                .layoutInflater
                .inflate(R.layout.info_window, null)
        // TODO("Показывать уникальное окно")
    }

}