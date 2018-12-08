package com.spencerbyson.gpstasks

import android.location.Location
import android.util.Log

class Task {
    val TAG = "GPSTasks-Task"
    var steps = ArrayList<Step>()

    fun updateLocation(loc : Location) {
        Log.i(TAG, loc.toString())
    }
}