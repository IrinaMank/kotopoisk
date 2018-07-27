package com.example.zapir.kotopoisk

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : Fragment(), OnMapReadyCallback {

    private var gmap: GoogleMap? = null

    companion object {

        fun newInstance(): MapFragment = MapFragment()
        private const val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        map_view.onCreate(savedInstanceState)
        map_view.getMapAsync(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MapFragment.MAP_VIEW_BUNDLE_KEY)

        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MapFragment.MAP_VIEW_BUNDLE_KEY, mapViewBundle)
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
        map_view.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        //TODO(Убрать фокус с New-York'а)
        gmap = googleMap
        gmap!!.setMinZoomPreference(12f)
        val ny = LatLng(40.7143528, -74.0059731)
        gmap!!.moveCamera(CameraUpdateFactory.newLatLng(ny))
    }

}
