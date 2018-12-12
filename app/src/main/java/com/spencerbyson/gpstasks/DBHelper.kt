package com.spencerbyson.gpstasks

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.location.Location
import android.util.Log


var DATABASE_NAME = "TaskDB2"
var DATABASE_VERSION = 1

var CREATE_TASK_TABLE = "CREATE TABLE TaskTable( id int primary key, taskName varchar(100) not null, enabled boolean);"
var CREATE_STEP_TABLE = "CREATE TABLE StepTable( id int primary key, type int, data varchar(100) not null);"
var CREATE_TASK_STEP_TABLE = "CREATE TABLE TaskStepsTable( taskID int, stepID int);"

var TABLE_NAME1 = "TaskTable"
var TABLE_NAME2 = "stepTable"
var TABLE_NAME3 = "TaskStepsTable"

var T1_COL_ID = "id"
var T1_COL_NAME = "taskName"
var T1_COL_ENABLED = "enabled"

var T2_COL_ID = "id"
var T2_COL_TYPE = "type"
var T2_COL_DATA = "data"

var T3_COL_TASK = "taskID"
var T3_COL_STEP = "stepID"

class DBHelper(context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME, null,
    DATABASE_VERSION
){
    override fun onCreate(db: SQLiteDatabase){
        db?.execSQL(CREATE_TASK_TABLE)
        db?.execSQL(CREATE_STEP_TABLE)
        db?.execSQL(CREATE_TASK_STEP_TABLE)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    //TODO Implement Database Population
    fun populate(){
        //T1
        insertTask(1, "task1")
        insertTask(2, "task2")

        //T2
        insertStep(1, 0, "DUMMYDATA1")
        insertStep(2, 0, "DUMMYDATA2")
        insertStep(3, 1, "DUMMYDATA3")
        insertStep(4, 1, "DUMMYDATA4")

        //T3
        insertStepTask(1, 1)
        insertStepTask(1, 2)
        insertStepTask(2, 3)
        insertStepTask(2, 4)
    }

    fun insertTask(id: Int, task: String){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(T1_COL_ID, id)
        cv.put(T1_COL_NAME, task)
        cv.put(T1_COL_ENABLED, true)
        db.insert(TABLE_NAME1, null, cv)
    }

    fun insertStep(id: Int, type: Int, data: String){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(T2_COL_ID, id)
        cv.put(T2_COL_TYPE, type)
        cv.put(T2_COL_DATA, data)
        db.insert(TABLE_NAME2, null, cv)
    }

    fun insertStepTask(task: Int, step: Int){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(T3_COL_TASK, task)
        cv.put(T3_COL_STEP, step)
        db.insert(TABLE_NAME3, null, cv)
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

    fun readStepTasks() : MutableList<TaskStepDataObj>{
        var list : MutableList<TaskStepDataObj> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME3
        var result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do{
                val task = result.getString(result.getColumnIndex(T3_COL_TASK)).toInt()
                val step = result.getString(result.getColumnIndex(T3_COL_STEP)).toInt()
                list.add(TaskStepDataObj(task, step))
            }while(result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    fun getTasks() : ArrayList<Task>{
        var list = ArrayList<Task>()
        var stepList = ArrayList<Step>()

        val db = this.writableDatabase
        val query1 = "SELECT * FROM " + TABLE_NAME1
        val data1 = db.rawQuery(query1, null)
        if(data1.moveToFirst()){
            do{
                val query2 = "SELECT * FROM " + TABLE_NAME2 + " INNER JOIN " + TABLE_NAME3 + " ON " + TABLE_NAME2 + ".id = " + TABLE_NAME3 + ".stepID WHERE " + TABLE_NAME3 + ".taskID = " + data1.getString(data1.getColumnIndex(T1_COL_ID))
                val data2 = db.rawQuery(query2, null)
                if(data2.moveToFirst()){
                        do{
                            stepList.add(makeStep(data2.getString(data2.getColumnIndex(T2_COL_ID)).toInt(), data2.getString(data2.getColumnIndex(T2_COL_TYPE)).toInt(), data2.getString(data2.getColumnIndex(T2_COL_DATA))))
                        }while(data2.moveToNext())
                    }
                list.add(Task(data1.getString(data1.getColumnIndex(T1_COL_NAME)), stepList, true))
            }while(data1.moveToNext())
        }
        return list
    }

    fun deleteData(table: String, column: String, target: Int){
        val db = this.writableDatabase
        db.delete(table, column + "=" + target, null)
        db.close()
    }

    fun makeStep(id: Int, type: Int, data: String) : Step{
        return LocStep(Location(""), 100.0, true)
    }
}