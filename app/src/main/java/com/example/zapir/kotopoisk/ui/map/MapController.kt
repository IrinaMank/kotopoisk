package com.example.zapir.kotopoisk.ui.map

import android.content.Context
import com.example.zapir.kotopoisk.common.PetType
import com.example.zapir.kotopoisk.model.Ticket
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import org.slf4j.LoggerFactory

class MapController(private val context: Context?) : MapInterface {

    private lateinit var map: GoogleMap
    private var visibleMarkers: MutableSet<Marker>? = null
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)

    fun onAttachMap(googleMap: GoogleMap) {
        map = googleMap
        map.setInfoWindowAdapter(InfoWindowAdapter(context))
        map.setOnInfoWindowClickListener { handlerOnInfoWindowClickListener() }
        map.uiSettings.isMapToolbarEnabled = false

        setDefaultPosition()

        // TODO( delete test code )******
        val ticket = Ticket(lat = 55.0481662, lng = 82.91109, type = PetType.CAT.value)
        addMarker(ticket)
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

        if (visibleMarkers == null) {
            visibleMarkers = mutableSetOf(marker)
        } else {
            visibleMarkers?.add(marker)
        }
    }

    override fun removeMarker(id: String) {
        logger.info("removeMarker: id=$id")

        val iterator = visibleMarkers?.iterator() ?: throw UnknownPetId()

        while (iterator.hasNext()) {
            val marker = iterator.next()
            if (marker.title == id) {
                marker.remove()
                iterator.remove()
                return
            }
        }

        throw PetNotFound("Pet with id: $id not found")
    }

    override fun moveTo(newPoint: LatLng) {
        val cameraPosition = CameraPosition.Builder().target(newPoint).zoom(12f).build()
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun updateVisibleMarkers(markers: Collection<Ticket>) {
        logger.info("Update ${markers.size} markers")

        visibleMarkers?.clear()
        map.clear()

        markers.forEach {
            addMarker(it)
        }
    }

    private fun handlerOnInfoWindowClickListener() {
        logger.info("Click on info window")
        // TODO("Сделать переход в полное объявление")
    }

    private fun setDefaultPosition() {
        val nsk = LatLng(54.9935416, 82.9403465)
        val cameraPosition = CameraPosition.Builder().target(nsk).zoom(12f).build()
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

}