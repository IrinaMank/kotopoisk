package com.example.zapir.kotopoisk.ui.map

import android.content.Context
import com.bumptech.glide.Glide
import com.example.zapir.kotopoisk.common.PetType
import com.example.zapir.kotopoisk.model.Ticket
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import org.slf4j.LoggerFactory
import android.graphics.Bitmap
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition


class MapController : MapInterface {

    private lateinit var map: GoogleMap
    private val markers: MutableMap<String, Marker> = mutableMapOf()
    private val tickets: MutableMap<String, Ticket> = mutableMapOf()
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    private lateinit var infoWindowAdapter: InfoWindowAdapter

    fun onAttachMap(context: Context?, googleMap: GoogleMap) {
        map = googleMap
        infoWindowAdapter = InfoWindowAdapter(context)
        map.setInfoWindowAdapter(infoWindowAdapter)
        map.setOnInfoWindowClickListener { handlerOnInfoWindowClickListener() }
        map.uiSettings.isMapToolbarEnabled = false
        map.setOnMarkerClickListener { handlerOnMarkerClickListener(it) }

        setDefaultPosition()
    }

    override fun addMarker(ticket: Ticket) {
        logger.info("addMarker: ${ticket.lat}, ${ticket.lng}")

        moveTo(LatLng(ticket.lat, ticket.lng))

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

    override fun moveTo(newPoint: LatLng) {
        val cameraPosition = CameraPosition.Builder().target(newPoint).zoom(12f).build()
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
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

    private fun handlerOnInfoWindowClickListener() {
        logger.info("Click on info window")
        // TODO("Сделать переход в полное объявление")
    }

    private fun handlerOnMarkerClickListener(marker: Marker): Boolean {
        val ticket: Ticket? = tickets[marker.title]

        Glide.with(infoWindowAdapter.getContext()!!)
                .asBitmap()
                .load(ticket?.photo?.url)
                /*.listener(TODO(!!!!!!))*/
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        infoWindowAdapter.ticket = ticket
                        infoWindowAdapter.photo = resource
                        marker.showInfoWindow()
                    }
                })

        return true
    }

    private fun setDefaultPosition() {
        val nsk = LatLng(54.9935416, 82.9403465)
        val cameraPosition = CameraPosition.Builder().target(nsk).zoom(12f).build()
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

}