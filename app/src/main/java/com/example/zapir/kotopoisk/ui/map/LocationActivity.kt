package com.example.zapir.kotopoisk.ui.map

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.data.model.Ticket
import com.example.zapir.kotopoisk.domain.common.PetType
import com.example.zapir.kotopoisk.ui.base.BaseActivity
import com.example.zapir.kotopoisk.ui.ticket.NewTicketFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_map_location.*

class LocationActivity : BaseActivity(), LoadListener, OnMapReadyCallback {

    private var mapController: MapController? = null
    private var petType: Int = PetType.DEFAULT.value

    companion object {

        private const val PET_POSITION_KEY = "If we establish a position not for the first time"
        private const val PET_TYPE_KEY = "It's very important, because default marker (with paws) ugly :("
        private const val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"

        fun newIntent(context: Context, petType: Int, position: LatLng?): Intent {
            return Intent(context, LocationActivity::class.java).apply {
                putExtra(Intent.EXTRA_TEXT + PET_TYPE_KEY, petType)

                if (position != null) {
                    putExtra(Intent.EXTRA_TEXT + PET_POSITION_KEY, position)
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_location)
        setResult(Activity.RESULT_CANCELED)

        petType = intent.getIntExtra(Intent.EXTRA_TEXT + PET_TYPE_KEY, PetType.DEFAULT.value)

        mapController = MapController()
        map_view.onCreate(savedInstanceState)
        map_view.getMapAsync(this)

        confirmation_button.setOnClickListener {
            handlerConfirmationButtonClickListener()
        }

        changing_button.setOnClickListener { handlerChangingButtonClickListener() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY)

        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }

        map_view.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        map_view.onResume()
    }

    override fun onStart() {
        super.onStart()
        map_view.onStart()
    }

    override fun onStop() {
        super.onStop()
        map_view.onStop()
    }

    override fun onPause() {
        map_view.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        map_view?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view?.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapController?.onAttachLocationMap(googleMap, petType)
        mapController?.loadListeners?.add(this)

        if (intent.hasExtra(Intent.EXTRA_TEXT + PET_POSITION_KEY)) {
            val position = intent.getParcelableExtra<LatLng>(Intent.EXTRA_TEXT + PET_POSITION_KEY)

            val ticket = Ticket(
                    type = petType,
                    lat = position.latitude,
                    lng = position.longitude
            )

            mapController?.addMarker(ticket)
            mapController?.moveTo(position)
            setLoadStart()
        }
    }

    override fun setLoadStart() {
        confirmation_button.visibility = View.VISIBLE
        changing_button.visibility = View.VISIBLE
    }

    override fun setLoadGone() {
        confirmation_button.visibility = View.GONE
        changing_button.visibility = View.GONE
    }

    private fun handlerConfirmationButtonClickListener() {
        // We take all tickets from the card
        val tickets = mapController!!.getAllTickets()

        // But in this map we have only one ticket
        val ticket = tickets[0]
        val position = LatLng(ticket.lat, ticket.lng)

        // Remove this ticket and put it on Intent
        mapController?.updateVisibleMarkers(setOf())
        val intent = Intent().apply {
            putExtra(Intent.EXTRA_TEXT + NewTicketFragment.GET_LOCATION_KEY, position)
        }

        setResult(RESULT_OK, intent)
        finish()
    }

    private fun handlerChangingButtonClickListener() {
        // Hack for clear map :)
        mapController?.updateVisibleMarkers(setOf())
        setLoadGone()
    }
}