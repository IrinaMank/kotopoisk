package com.example.zapir.kotopoisk.ui.map

import com.example.zapir.kotopoisk.data.model.Ticket
import com.google.android.gms.maps.model.LatLng

interface MapInterface {

    fun addMarker(ticket: Ticket)
    fun removeMarker(id: String)
    fun moveTo(newPoint: LatLng)
    fun updateVisibleMarkers(newTickets: Collection<Ticket>)

}