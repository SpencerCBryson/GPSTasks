package com.spencerbyson.gpstasks

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import android.telephony.SmsManager
import android.util.Log


@Parcelize
class SMSAction(val target : String, val msg : String) : Action(1), Parcelable {
    val TAG = "GPSTasks-SMSAction"

    override fun execute() {
        try {
            val smgr = SmsManager.getDefault()
            smgr.sendTextMessage(target, null, msg, null, null)
            Log.i(TAG, "Sent SMS!")
        } catch (e : Exception) {
            Log.i(TAG, "SMS failed to send")
        }
    }

}