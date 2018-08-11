package com.zapir.kotopoisk.ui.map

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.zapir.kotopoisk.R
import com.zapir.kotopoisk.data.exceptions.PetNotFound
import com.zapir.kotopoisk.data.model.Ticket
import com.zapir.kotopoisk.domain.bottomBarApi.TransactionUtils
import com.zapir.kotopoisk.domain.common.PetType
import com.zapir.kotopoisk.ui.base.BaseActivity
import com.zapir.kotopoisk.ui.ticket.OverviewTicketFragment
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.slf4j.LoggerFactory

class MapController : MapInterface {

    val loadListeners: MutableList<LoadListener> = mutableListOf()
    private lateinit var map: GoogleMap
    private val markers: MutableMap<String, Marker> = mutableMapOf()
    private val tickets: MutableMap<String, Ticket> = mutableMapOf()
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    private lateinit var infoWindowAdapter: InfoWindowAdapter
    private var petType: Int = PetType.DEFAULT.value

    private fun onAttachMap(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isMapToolbarEnabled = false
    }

    fun onAttachMainMap(context: Context?, googleMap: GoogleMap) {
        // Map settings
        onAttachMap(googleMap)

        // Info window
        infoWindowAdapter = InfoWindowAdapter(context)
        map.setInfoWindowAdapter(infoWindowAdapter)
        map.setOnInfoWindowClickListener { handlerOnInfoWindowClickListener(context) }

        // Marker settings
        map.setOnMarkerClickListener { handlerOnMarkerClickListener(it) }
        setDefaultPosition()
    }

    fun onAttachLocationMap(googleMap: GoogleMap, petType: Int) {
        // Map settings
        onAttachMap(googleMap)
        map.setOnMapClickListener { mapClickListener(it) }
        setDefaultPosition()

        // Marker settings
        this.petType = petType
        map.setOnMarkerClickListener {
            return@setOnMarkerClickListener true
        }
    }


    override fun addMarker(ticket: Ticket) {
        logger.info("addMarker: ${ticket.lat}, ${ticket.lng}")

        val markerOptions = MarkerOptions().apply {
            this.icon(IconFactory.getBitmapDescriptionPetIcon(ticket.type))
            this.position(LatLng(ticket.lat, ticket.lng))
            this.title(ticket.id)
        }

        val marker = map.addMarker(markerOptions)
        markers[ticket.id] = marker
        tickets[ticket.id] = ticket
    }

    override fun removeMarker(id: String) {
        logger.info("removeMarker: id=$id")
        markers.remove(id)?.remove()
        tickets.remove(id)
    }

    override fun moveTo(newPoint: LatLng, zoom: Float) {
        val cameraPosition = CameraPosition.Builder().target(newPoint).zoom(zoom).build()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun updateVisibleMarkers(newTickets: Collection<Ticket>) {
        logger.info("Update ${newTickets.size} markers")

        this.tickets.clear()
        this.markers.clear()
        map.clear()

        newTickets.forEach {
            addMarker(it)
        }
    }

    override fun getAllTickets(): List<Ticket> {
        return tickets.values.toList()
    }

    private fun handlerOnInfoWindowClickListener(context: Context?) {
        logger.info("Click on info window")
        val manager = (context as BaseActivity).supportFragmentManager
        val ticket = infoWindowAdapter.ticket

        if (ticket != null) {
            TransactionUtils.replaceFragment(manager, R.id.container, OverviewTicketFragment.newInstance(ticket))
        }
    }

    private fun handlerOnMarkerClickListener(marker: Marker): Boolean {
        val ticket: Ticket? = tickets[marker.title]

        if (ticket == null) {
            throw PetNotFound("Ticket with id ${marker.title} not found")
        } else if (ticket.photo.url.isEmpty()) {
            infoWindowAdapter.ticket = ticket
            infoWindowAdapter.photo = null
            return false
        }

        loadListeners.forEach {
            it.setLoadStart()
        }

        Glide.with(infoWindowAdapter.getContext()!!)
                .asBitmap()
                .load(ticket.photo.url)
                .listener(photoLoadListener)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        infoWindowAdapter.ticket = ticket
                        infoWindowAdapter.photo = resource
                        marker.showInfoWindow()
                        val cam = CameraUpdateFactory.newLatLng(marker.position)
                        map.animateCamera(cam)
                    }
                })

        return true
    }

    private fun setDefaultPosition() {
        val nsk = LatLng(54.9935416, 82.9403465)
        val cameraPosition = CameraPosition.Builder().target(nsk).zoom(12f).build()
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private val photoLoadListener = object : RequestListener<Bitmap> {
        override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
        ): Boolean {

            loadListeners.forEach {
                it.setLoadGone()
            }

            return false
        }
    }

    private fun mapClickListener(position: LatLng) {
        // Не очень красивый код, но другого у меня нет :(
        if (tickets.size == 1) {
            return
        }

        val ticket = Ticket(
                type = petType,
                lat = position.latitude,
                lng = position.longitude
        )

        addMarker(ticket)

        loadListeners.forEach {
            it.setLoadStart()
        }
    }

}
