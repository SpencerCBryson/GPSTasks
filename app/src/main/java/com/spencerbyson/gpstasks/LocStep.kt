package com.spencerbyson.gpstasks

import android.location.Location
import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class LocStep(val _action : @RawValue Action, val target : Location, val radius : Double) : Step(1, _action), Parcelable {

    val TAG = "GPSTasks-LocStep"

    fun predicate(loc : Location) : Boolean {
        val distance = target.distanceTo(loc)
        Log.i(TAG, "Distance: $distance")

        return distance <= radius
    }

    override fun action() {
        Log.i(TAG, "Executing action")
        action.execute()
    }
}