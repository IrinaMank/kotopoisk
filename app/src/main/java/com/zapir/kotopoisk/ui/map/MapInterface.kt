package com.zapir.kotopoisk.ui.map

import com.zapir.kotopoisk.data.model.Ticket
import com.google.android.gms.maps.model.LatLng

interface MapInterface {

    fun addMarker(ticket: Ticket)
    fun removeMarker(id: String)
    fun moveTo(newPoint: LatLng, zoom: Float = 12f)
    fun updateVisibleMarkers(newTickets: Collection<Ticket>)
    fun getAllTickets(): List<Ticket>

}