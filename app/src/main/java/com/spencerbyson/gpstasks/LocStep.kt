package com.spencerbyson.gpstasks

import android.location.Location
import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class LocStep(val target : Location, val radius : Double, val withinRadius : Boolean) : Step(1, false), Parcelable {

    val TAG = "GPSTasks-LocStep"
    val typeTag = "Entered Radius"

    fun predicate(loc: Location): Boolean {
        val distance = target.distanceTo(loc)
        Log.i(TAG, "Distance: $distance")

        return if (withinRadius) // location inside radius
            distance <= radius
        else // location outside radius
            distance >= radius
    }

    override fun toString() : String{
        return target.toString() + "," +radius.toString() + "," + withinRadius.toString()
    }

    override fun execute() {}
}

