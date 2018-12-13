package com.spencerbyson.gpstasks

import android.content.Intent
import android.graphics.Camera
import android.location.Location
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_configure_task_location.*



class ConfigureTaskLocation : AppCompatActivity(), OnMapReadyCallback, PlaceSelectionListener {

    private lateinit var mMap: GoogleMap
    private lateinit var selection : Circle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configure_task_location)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        // not sure what the ideal non-deprecated alternative is to fragmentManager
        val autocompleteFragment = fragmentManager
            .findFragmentById(R.id.placeSearch) as PlaceAutocompleteFragment
        autocompleteFragment.setOnPlaceSelectedListener(this)
    }

    override fun onPlaceSelected(place : Place?) {
        val cameraUpdate = CameraPosition.builder()
            .zoom(20.0f)
            .target(place?.latLng)
            .build()

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraUpdate))
        updateSelection()
    }

    override fun onError(p0: Status?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun location(latlng : LatLng) = Location(LocationManager.GPS_PROVIDER).apply {
        longitude = latlng.longitude
        latitude = latlng.latitude
    }

    fun newCenter() = mMap.cameraPosition.target

    fun newRadius() : Double {
        val projection = mMap.projection

        val left = location(projection.visibleRegion.nearLeft)
        val right = location(projection.visibleRegion.nearRight)
        val distance = left.distanceTo(right).toDouble()
        val radius = distance / 2 - (0.1 * distance)

        return radius
    }

    fun updateSelection() {
        val center = newCenter()
        val radius = newRadius()

        updateCircle(center, radius)
    }

    fun updateCircle(location : LatLng, radius : Double) {
        val circleOptions = CircleOptions().apply {
            center(mMap.cameraPosition.target)
            radius(1000.0)
        }

        selection.radius = radius
        selection.center = location
    }

    fun selectLocation(view : View) {
        val step = LocStep(location(selection.center), selection.radius, true)
        val data = Intent()
        data.putExtra("step", step)

        setResult(1, data)
        finish()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)

        mMap.setOnCameraIdleListener { updateSelection() }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10.0f))

        val cOptions = CircleOptions().apply {
            center(newCenter())
            radius(newRadius())
            fillColor(ResourcesCompat.getColor(resources, R.color.mapFill, null))
            strokeColor(ResourcesCompat.getColor(resources, R.color.mapStroke, null))
        }

        selection = mMap.addCircle(cOptions)
        mMap.setOnCameraMoveListener { updateSelection() }
        mMap.setOnCameraIdleListener {  }
    }
}
