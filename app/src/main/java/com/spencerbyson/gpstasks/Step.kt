package com.spencerbyson.gpstasks

abstract class Step(type : Int) {
    companion object {
        val LOCATION_STEP = 1
        val TIME_STEP = 2
    }
}

