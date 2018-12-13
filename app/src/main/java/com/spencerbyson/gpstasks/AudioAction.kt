package com.spencerbyson.gpstasks

import android.content.Context
import android.location.Location
import android.media.MediaPlayer
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue



@Parcelize
class AudioAction(val context: @RawValue Context) : Step(3, false), Parcelable {

    override fun execute() {
        val mp = MediaPlayer.create(context, R.raw.hey)
        mp.start()
    }
}