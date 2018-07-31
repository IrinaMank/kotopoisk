package com.example.zapir.kotopoisk.ui.map

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import com.bumptech.glide.Glide
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.model.Ticket
import com.example.zapir.kotopoisk.ui.BaseActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_ensuring.view.*
import kotlinx.android.synthetic.main.info_window.view.*

class InfoWindowAdapter(private val context: Context?) : GoogleMap.InfoWindowAdapter {

    var ticket: Ticket? = null
    var photo: Bitmap? = null

    override fun getInfoWindow(marker: Marker?): View? {
        return null
    }

    fun getContext(): Context? = context

    override fun getInfoContents(marker: Marker?): View? {
        val view = (context as Activity)
                .layoutInflater
                .inflate(R.layout.info_window, null)

        view.info_image.setImageBitmap(photo)
        view.info_overview.text = ticket?.overview
        view.info_date.text = ticket?.date
        view.info_phone_number.text = ticket?.user?.phone

        return view
    }

}