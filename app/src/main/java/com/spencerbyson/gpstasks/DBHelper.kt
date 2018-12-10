

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


var DATABASE_NAME = "TaskDB"
var DATABASE_VERSION = 1

var DATABASE_CREATE1 = "CREATE TABLE TaskTable( id int primary key, taskName varchar(100) not null);"
var DATABASE_CREATE2 = "CREATE TABLE StepTable( id int primary key, type int, data varchar(100) not null);"
var DATABASE_CREATE3 = "CREATE TABLE TaskStepsTable( taskID int primary key, stepID int);"

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

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase){
        db?.execSQL(DATABASE_CREATE1)
        db?.execSQL(DATABASE_CREATE2)
        db?.execSQL(DATABASE_CREATE3)
        populate()
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    //TODO Implement Database Population
    fun populate(){

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


    fun readData() : MutableList<String>{
        var list : MutableList<String> = ArrayList()
//        val db = this.readableDatabase
//        val query = "Select * from " + TABLE_NAME
//        val result = db.rawQuery(query, null)
//        if(result.moveToFirst()){
//            do {
//
//                list.add(product)
//
//            }while (result.moveToNext())
//        }
//
//        result.close()
//        db.close()

        return list
    }

    fun deleteData(table: String, column: String, target: Int){
        val db = this.writableDatabase
        db.delete(table, column + "=" + target, null)
        db.close()
    }
}