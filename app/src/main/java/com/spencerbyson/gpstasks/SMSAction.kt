package com.spencerbyson.gpstasks

import android.content.Context
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import android.telephony.TelephonyManager
import android.content.Context.TELEPHONY_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.telephony.SmsManager
import android.util.Log


@Parcelize
class SMSAction(val target : String, val msg : String) : Action(1), Parcelable {
    val TAG = "GPSTasks-SMSAction"

    fun execute() {
        try {
            val smgr = SmsManager.getDefault()
            smgr.sendTextMessage(target, null, msg, null, null)
            Log.i(TAG, "Sent SMS!")
        } catch (e : Exception) {
            Log.i(TAG, "SMS failed to send")
        }
    }

}