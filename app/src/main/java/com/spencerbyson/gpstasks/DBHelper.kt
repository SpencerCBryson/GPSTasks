package com.spencerbyson.gpstasks

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.location.Location
import android.util.Log


var DATABASE_NAME = "TaskDB6"
var DATABASE_VERSION = 1

var CREATE_TASK_TABLE = "CREATE TABLE TaskTable( id int primary key, taskName varchar(100) not null, enabled boolean);"
var CREATE_STEP_TABLE = "CREATE TABLE StepTable( id int primary key, type int, data varchar(100) not null, taskID varchar(100));"

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

    val TAG = "GPSTasks-DBHelper"

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

    fun insertTask(task: Task){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(T1_COL_NAME, task.title)
        cv.put(T1_COL_ENABLED, task.enabled)

        db.insert(TABLE_NAME1, null, cv)
    }

    fun insertStep(type: Int, data: String, taskID: String){
        val db = this.writableDatabase
        var cv = ContentValues()

        cv.put(T2_COL_TASK, taskID)
        cv.put(T2_COL_TYPE, type)
        cv.put(T2_COL_DATA, data)
        db.insert(TABLE_NAME2, null, cv)
    }

    fun updateOrInsertStep(type: Int, data: String, taskID: String, oldTitle: String){
        val db = this.writableDatabase
        var cv = ContentValues()

        cv.put(T2_COL_TASK, taskID)
        cv.put(T2_COL_TYPE, type)
        cv.put(T2_COL_DATA, data)

        val u = db.update(TABLE_NAME2, cv, "taskID=?", arrayOf(oldTitle))
        if (u == 0)
            db.insert(TABLE_NAME2, null, cv)
    }


    fun addTask(task: Task){
        insertTask(task)
        task.steps.forEach{
            insertStep(it.type, it.toString(), task.title)
        }
    }

    fun updateTask(task : Task, oldTitle : String) {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(T1_COL_NAME, task.title)
        cv.put(T1_COL_ENABLED, task.enabled)

        db.update(TABLE_NAME1, cv, "taskName=?", arrayOf(oldTitle))

        task.steps.forEach{
            updateOrInsertStep(it.type, it.toString(), task.title, oldTitle)
        }

    }


    fun getTasks() : ArrayList<Task> {
        var list = ArrayList<Task>()

        val db = this.readableDatabase
        val query1 = "SELECT * FROM " + TABLE_NAME1
        val data1 = db.rawQuery(query1, null) //tasks
        if(data1.moveToFirst()){
            do{
                val name = data1.getString(data1.getColumnIndex(T1_COL_NAME))
                //val taskId = data1.getInt(data1.getColumnIndex(T1_COL_ID))
                val enabledInt = data1.getInt(data1.getColumnIndex(T1_COL_ENABLED))

                var enabled = true

                if(enabledInt == 0)
                    enabled = false

                val stepList = ArrayList<Step>()
                //val query2 = "SELECT * FROM " + TABLE_NAME2 + " WHERE " + taskId + " = " + T2_COL_TASK
                val query2 = "SELECT * FROM $TABLE_NAME2 WHERE $T2_COL_TASK = \"$name\""
                val data2 = db.rawQuery(query2, null) //steps

                //Log.i(TAG, "count: ${data2.count}")

                if(data2.moveToFirst()){
                    do{
                        val type = data2.getInt(data2.getColumnIndex(T2_COL_TYPE))
                        val dataStr = data2.getString(data2.getColumnIndex(T2_COL_DATA))

                        val step = makeStep(type, dataStr)

                        stepList.add(step)
                    }while(data2.moveToNext())
                }

                //Log.i(TAG, "steps: ${stepList.size.toString()}")
                val task = Task(name, stepList, enabled)
                list.add(task)
            }while(data1.moveToNext())
        }

        //Log.i(TAG, list.size.toString())

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
        }else if(type == 2) { //SMS
            val splitdata = data.split("|")
            return SMSAction(splitdata[0], splitdata[1])
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