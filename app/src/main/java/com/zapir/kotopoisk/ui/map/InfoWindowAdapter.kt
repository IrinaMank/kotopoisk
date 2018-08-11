package com.zapir.kotopoisk.ui.map

import android.app.Activity
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.graphics.Bitmap
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import com.zapir.kotopoisk.R
import com.zapir.kotopoisk.data.model.Ticket
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.info_window.view.*

class InfoWindowAdapter(private val context: Context?) : GoogleMap.InfoWindowAdapter {

    var ticket: Ticket? = null
    var photo: Bitmap? = null

    override fun getInfoWindow(marker: Marker?): View? {
        val wrapper = ContextThemeWrapper(context, R.style.TransparentBackground)
        val inflater = wrapper.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return inflater.inflate(R.layout.info_window, null).fillView()
    }

    override fun getInfoContents(marker: Marker?): View? {
        val view = (context as Activity)
                .layoutInflater
                .inflate(R.layout.info_window, null)

        return view.fillView()
    }

    fun getContext(): Context? = context

    private fun View.fillView(): View {
        this.info_image.setImageBitmap(photo)
        this.info_overview.text = ticket?.overview

        if (this.info_overview.text.isEmpty()) {
            this.info_overview.visibility = View.GONE
        }

        this.info_date.text = ticket?.date
        this.info_phone_number.text = ticket?.user?.phone

        return this
    }
}
