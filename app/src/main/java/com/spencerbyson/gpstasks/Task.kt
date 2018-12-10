package com.spencerbyson.gpstasks

import android.location.Location
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class Task(var title : String, var steps : @RawValue ArrayList<Step>, var enabled : Boolean = true) : Parcelable {
    val TAG = "GPSTasks-Task"

    fun updateLocation(loc : Location) {
        steps.forEach {
            if(it.type == LOCATION_STEP) {
                val locStep = it as LocStep
                if (locStep.predicate(loc)) {
                    // location within target
                    locStep.action()
                }
            }
        }
    }



    companion object {
        val LOCATION_STEP = 1
        val TIME_STEP = 2
    }
}