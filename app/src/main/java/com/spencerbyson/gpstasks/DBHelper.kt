package com.spencerbyson.gpstasks

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


var DATABASE_NAME = "TaskDB"
var DATABASE_VERSION = 1

var CREATE_TASK_TABLE = "CREATE TABLE TaskTable( id int primary key, taskName varchar(100) not null);"
var CREATE_STEP_TABLE = "CREATE TABLE StepTable( id int primary key, type int, data varchar(100) not null);"
var CREATE_TASK_STEP_TABLE = "CREATE TABLE TaskStepsTable( taskID int, stepID int);"

var TABLE_NAME1 = "TaskTable"
var TABLE_NAME2 = "stepTable"
var TABLE_NAME3 = "TaskStepTable"

var T1_COL_ID = "id"
var T1_COL_NAME = "taskName"

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
        insertTask(1, "task1")
        insertTask(2, "task2")
        insertTask(3, "task3")
    }

    fun insertTask(id: Int, task: String){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(T1_COL_ID, id)
        cv.put(T1_COL_NAME, task)
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
                Log.d("nice", "data")
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

    fun deleteData(table: String, column: String, target: Int){
        val db = this.writableDatabase
        db.delete(table, column + "=" + target, null)
        db.close()
    }
}