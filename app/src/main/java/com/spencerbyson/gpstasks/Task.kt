package com.spencerbyson.gpstasks

import android.location.Location
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class Task(var title : String, var steps : @RawValue ArrayList<Step>, var enabled : Boolean = true) : Parcelable {
    val TAG = "GPSTasks-Task"

    lateinit var handler : TaskHandler
    //var id : Int = -1

    fun updateLocation(loc : Location) {
        steps.forEach checkSteps@{
            if (it.finished)
                return@checkSteps

            if(it is LocStep) {
                System.out.println("*********** checking loc")
                val locStep = it as LocStep
                if (locStep.predicate(loc)) {
                    // location within target
                    locStep.finished = true
                } else {
                    return
                }
            } else if (it is SMSAction) {
                System.out.println("############# doing action")
                it.execute()
                it.finished = true
            }
        }

//        resetSteps()
    }

    fun resetSteps() {
        steps.forEach {
            it.finished = false
        }
    }

    companion object {
        val LOCATION_STEP = 1
        val TIME_STEP = 2
    }
}