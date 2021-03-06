package com.spencerbyson.gpstasks

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import android.telephony.SmsManager
import android.util.Log


@Parcelize
class SMSAction(val target : String, val msg : String) : Step(2, false), Parcelable {
    val TAG = "GPSTasks-SMSAction"
    val typeTag = "Send SMS"

    override fun execute() {
        try {
            val smgr = SmsManager.getDefault()
            smgr.sendTextMessage(target, null, msg, null, null)
            Log.i(TAG, "Sent SMS!")
            //TODO: call some listener in Task Service to update UI to reflect step & action completion
        } catch (e : Exception) {
            Log.i(TAG, "SMS failed to send")
        }
    }

    override fun toString(): String {
       return "$target|$msg"
    }


}