package com.spencerbyson.gpstasks

import android.location.Location
import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class LocStep(val _action : @RawValue Action, val target : Location, val radius : Double, val withinRadius : Boolean) : Step(1, _action, false), Parcelable {

    val TAG = "GPSTasks-LocStep"

    fun predicate(loc : Location) : Boolean {
        val distance = target.distanceTo(loc)
        Log.i(TAG, "Distance: $distance")

        return if(withinRadius) // location inside radius
            distance <= radius
        else // location outside radius
            distance >= radius
    }

    override fun action() {
        Log.i(TAG, "Executing action")
        action.execute()
        finished = true
    }
}