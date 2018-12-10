package com.spencerbyson.gpstasks

abstract class Step(val type : Int, val action : Action) {
    abstract fun action()
}