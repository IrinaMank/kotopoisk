package com.zapir.kotopoisk.ui.map

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zapir.kotopoisk.KotopoiskApplication
import com.zapir.kotopoisk.R
import com.zapir.kotopoisk.domain.photo.PhotoDialog
import com.zapir.kotopoisk.ui.base.BaseFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.concurrent.TimeUnit

class MapFragment : BaseFragment(), OnMapReadyCallback, LoadListener {

    private var mapController: MapController? = null

    companion object {

        private const val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"
        fun newInstance(): MapFragment = MapFragment()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapController = MapController()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map_view.onCreate(savedInstanceState)
        map_view.getMapAsync(this)
        map_fab.setOnClickListener { handlerFloatActionBar() }
        toolbar_title.text = getString(R.string.toolbar_string_map)

        if (KotopoiskApplication.preferencesManager().isMapHint()) {
            invisible_view.visibility = View.VISIBLE
            map_hint.visibility = View.VISIBLE
        }

        invisible_view.setOnClickListener {
            map_hint.visibility = View.GONE
            invisible_view.visibility = View.GONE
        }
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
        mapController?.onAttachMainMap(context, googleMap)
        mapController?.loadListeners?.add(this)

        ticketController.getAllTickets()
                .timeout(R.integer.timeout.toLong(), TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    setLoadStart()
                }
                .doFinally {
                    setLoadGone()
                }
                .subscribe(
                        {
                            mapController?.updateVisibleMarkers(it)
                            listenToNewTickets()
                        },
                        {

                        }
                )
    }

    private fun listenToNewTickets() {
        KotopoiskApplication.rxBus()
                .listenForNewTickets()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    setLoadStart()
                }
                .doFinally {
                    setLoadGone()
                }
                .subscribe {
                    mapController?.moveTo(LatLng(it.lat, it.lng))
                }
    }

    private fun handlerFloatActionBar() {
        logger.info("Click on float action bar")
        invisible_view.visibility = View.GONE
        map_hint.visibility = View.GONE
        callDialog()
    }

    private fun callDialog() {
        val dialog = PhotoDialog()
        dialog.show(activity?.supportFragmentManager, "PhotoDialog")
    }

    override fun setLoadStart() {
        progress_bar.visibility = View.VISIBLE
        val animation = progress_bar.background as AnimationDrawable
        animation.start()
    }

    override fun setLoadGone() {
        progress_bar.visibility = View.GONE
    }

}
