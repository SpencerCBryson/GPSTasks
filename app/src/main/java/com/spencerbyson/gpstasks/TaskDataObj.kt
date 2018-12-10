package com.spencerbyson.gpstasks

data class TaskDataObj(val id: Int, val name: String){
    override fun toString(): String{
        return name
    }
}