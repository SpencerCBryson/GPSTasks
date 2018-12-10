package com.spencerbyson.gpstasks

abstract class Action(val type : Int) {
    abstract fun execute()
}