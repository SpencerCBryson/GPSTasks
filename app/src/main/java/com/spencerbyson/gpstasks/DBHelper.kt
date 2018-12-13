package com.spencerbyson.gpstasks

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.location.Location
import android.util.Log


var DATABASE_NAME = "TaskDB4"
var DATABASE_VERSION = 1

var CREATE_TASK_TABLE = "CREATE TABLE TaskTable( id int primary key, taskName varchar(100) not null, enabled boolean);"
var CREATE_STEP_TABLE = "CREATE TABLE StepTable( id int primary key, type int, data varchar(100) not null, taskID int);"

var TABLE_NAME1 = "TaskTable"
var TABLE_NAME2 = "stepTable"

var T1_COL_ID = "id"
var T1_COL_NAME = "taskName"
var T1_COL_ENABLED = "enabled"

var T2_COL_ID = "id"
var T2_COL_TYPE = "type"
var T2_COL_DATA = "data"
var T2_COL_TASK = "taskID"


class DBHelper(context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME, null,
    DATABASE_VERSION
){
    override fun onCreate(db: SQLiteDatabase){
        db?.execSQL(CREATE_TASK_TABLE)
        db?.execSQL(CREATE_STEP_TABLE)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun populate(){
        val loc = Location("")
        loc.latitude = 10.0
        loc.longitude = 10.0
        val s1 = LocStep(loc, 10.0, false)
        val s2 = SMSAction("6474639574", "nice")
        val t = Task("DummyTask", arrayListOf(s1, s2), true)
        addTask(t)
    }

    fun insertTask(task: String){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(T1_COL_NAME, task)
        cv.put(T1_COL_ENABLED, true)
        db.insert(TABLE_NAME1, null, cv)
    }

    fun insertStep(type: Int, data: String, taskID: Int){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(T2_COL_TYPE, type)
        cv.put(T2_COL_DATA, data)
        db.insert(TABLE_NAME2, null, cv)
    }

    fun addTask(task: Task){
        insertTask(task.title)
        task.steps.forEach{
            insertStep(it.type, it.toString(), task.id)
        }
    }

    fun getTasks() : ArrayList<Task>{
        var list = ArrayList<Task>()
        var stepList = ArrayList<Step>()

        val db = this.writableDatabase
        val query1 = "SELECT * FROM " + TABLE_NAME1
        val data1 = db.rawQuery(query1, null)
        if(data1.moveToFirst()){
            do{
                val query2 = "SELECT * FROM " + TABLE_NAME2 + " WHERE " + data1.getString(data1.getColumnIndex(T1_COL_ID)) + " = " + T2_COL_TASK
                val data2 = db.rawQuery(query2, null)
                if(data2.moveToFirst()){
                        do{
                            stepList.add(makeStep(data2.getString(data2.getColumnIndex(T2_COL_TYPE)).toInt(), data2.getString(data2.getColumnIndex(T2_COL_DATA))))
                        }while(data2.moveToNext())
                    }
                list.add(Task(data1.getString(data1.getColumnIndex(T1_COL_NAME)), stepList, true))
            }while(data1.moveToNext())
        }
        return list
    }

    fun makeStep(type: Int, data: String) : Step{
            //Type 0 = Error
        if(type == 0){
            Log.d("nice", "ERROR")

            //Type 1 = LocStep
        }else if(type == 1){
            val splitdata = data.split(",")
            val loc = Location("")
            loc.latitude=splitdata[0].toDouble()
            loc.longitude=splitdata[1].toDouble()
            return LocStep(loc, splitdata[2].toDouble(), splitdata[3].toBoolean())
        }

        //Returning Dummy Data, occurs if error.
        return LocStep(Location(""), 100.0, true)
    }

    fun changeTaskState(id: Int, newState: Boolean){
        val db = this.writableDatabase
        val query1 = "UPDATE TaskTable SET enabled = " + newState + " WHERE id = " + id.toString()
        val data1 = db.rawQuery(query1, null)
    }
}