package com.example.zapir.kotopoisk.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.photo.PhotoDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : BaseFragment(), OnMapReadyCallback {

    private var gmap: GoogleMap? = null

    companion object {

        fun newInstance(): MapFragment = MapFragment()
        private const val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"

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
        map_fab.setOnClickListener { callDialog() }
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
        map_view.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        //TODO(Убрать фокус с NSK)
        gmap = googleMap
        val nsk = LatLng(54.9935416, 82.9403465)
        val cameraPosition = CameraPosition.Builder().target(nsk).zoom(12f).build()
        gmap!!.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun callDialog(){
        val dialog = PhotoDialog()
        dialog.show(activity?.supportFragmentManager, "PhotoDialog")
    }

}

/*class MapFragment : BaseFragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_map, container, false)
        map_view.onCreate(savedInstanceState)
        map_view.onResume() // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(activity?.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        map_view.getMapAsync { mMap ->
            googleMap = mMap

            // For showing a move to my location button
            //googleMap!!.isMyLocationEnabled = true

            // For dropping a marker at a point on the Map
            val sydney = LatLng(-34.0, 151.0)
            googleMap!!.addMarker(MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"))

            // For zooming automatically to the location of the marker
            val cameraPosition = CameraPosition.Builder().target(sydney).zoom(12f).build()
            googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }

        return rootView
    }
}*/
