package com.spencerbyson.gpstasks

import android.location.Location
import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class Task(val steps : @RawValue ArrayList<Step>) : Parcelable {
    val TAG = "GPSTasks-Task"
    //var steps = ArrayList<Step>()

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