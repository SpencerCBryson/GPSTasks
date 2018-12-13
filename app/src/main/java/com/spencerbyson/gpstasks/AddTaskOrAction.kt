package com.spencerbyson.gpstasks

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.MapsInitializer
import com.spencerbyson.gpstasks.R.id.mapView
import com.google.android.gms.maps.MapView
import kotlinx.android.synthetic.main.map_preview.view.*
import org.w3c.dom.Text


class MapPreview() : DialogFragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.map_preview, container, false)

        mapView = v.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(this)

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        try {
            MapsInitializer.initialize(this.activity!!)
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }


        return v
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        System.out.println("hurrah")

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

    }
}

interface TextListener {
    fun optionSelected(view : View, text : String, phoneNumber : String)
}

class SMSActionFragment(val listener : TextListener) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.sms_action, container, false)

        val button = v.findViewById<Button>(R.id.save_SMS)
        button.setOnClickListener {
            val smsText = v.findViewById<EditText>(R.id.sms_content).text.toString()
            val phoneNumberText = v.findViewById<EditText>(R.id.sms_number).text.toString()

            listener.optionSelected(it, smsText, phoneNumberText)
        }

        return v
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(listener: TextListener) = SMSActionFragment(listener)
    }
}

class AddTaskOrAction : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_task_or_action, container, false)
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = AddTaskOrAction()
    }
}
