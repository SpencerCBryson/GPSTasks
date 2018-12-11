package com.spencerbyson.gpstasks


abstract class Step(val type : Int, var finished : Boolean) {
    abstract fun execute()
}