package com.spencerbyson.gpstasks

abstract class Step(val type : Int, val action : Action, var finished : Boolean) {
    abstract fun action()

}