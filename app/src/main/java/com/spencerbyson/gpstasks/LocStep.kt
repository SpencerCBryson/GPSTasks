package com.spencerbyson.gpstasks

import android.location.Location
import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize

@Parcelize
class LocStep(val target : Location, val radius : Double) : Step(1), Parcelable {

    val TAG = "GPSTasks-LocStep"

    fun predicate(loc : Location) : Boolean {
        val distance = target.distanceTo(loc)
        Log.i(TAG, "Distance: $distance")

        return distance <= radius
    }

    fun action() {
        Log.i(TAG, "Loc within target!!!")
    }
}