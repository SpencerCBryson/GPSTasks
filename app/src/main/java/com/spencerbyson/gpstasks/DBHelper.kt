package com.spencerbyson.gpstasks

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.location.Location
import android.util.Log


var DATABASE_NAME = "TaskDB3"
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

    //TODO Implement Database Population
    fun populate(){

        //T1
        insertTask("task1")
        insertTask("task2")

        //T2
        insertStep(1, "DUMMYDATA1", 1)
        insertStep(1, "DUMMYDATA2", 1)
        insertStep(1, "DUMMYDATA3", 2)
        insertStep(1, "DUMMYDATA4", 2)


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

    fun readTasks() : MutableList<TaskDataObj>{
        var list : MutableList<TaskDataObj> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME1
        var result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do{
                val id = result.getString(result.getColumnIndex(T1_COL_ID)).toInt()
                val name = result.getString(result.getColumnIndex(T1_COL_NAME))
                list.add(TaskDataObj(id, name))
            }while(result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    fun readSteps() : MutableList<StepDataObj>{
        var list : MutableList<StepDataObj> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME2
        var result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do{
                val id = result.getString(result.getColumnIndex(T2_COL_ID)).toInt()
                val type = result.getString(result.getColumnIndex(T2_COL_TYPE)).toInt()
                val data = result.getString(result.getColumnIndex(T2_COL_DATA))
                list.add(StepDataObj(id, type, data))
            }while(result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    fun addTask(task: Task){
        insertTask(task.title)
        task.steps.forEach{
            //TODO MAKE THIS WORK
            //insertStep(it)
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
            return LocStep(Location(splitdata[0]), splitdata[1].toDouble(), splitdata[2].toBoolean())
        }

        //Returning Dummy Data, occurs if error.
        return LocStep(Location(""), 100.0, true)
    }
}