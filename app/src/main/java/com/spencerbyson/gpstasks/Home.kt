package com.spencerbyson.gpstasks

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout

class Home : AppCompatActivity() {

    var locGranted = false
    val TAG = "GPSTasks-Main"

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("nice", requestCode.toString() + resultCode.toString() + data.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        getPerms()

        //TODO Change this to populate from the SQLite database of tasks.
        val rvlist = arrayListOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        val rv = findViewById<RecyclerView>(R.id.mRecyclerView)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rv.adapter = MainRecyclerAdapter(rvlist)

        val addButton = findViewById<Button>(R.id.mAddButton)
        addButton.setOnClickListener{
            val intent = Intent(this, AddTask::class.java)
            startActivityForResult(intent, 1)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun getPerms () {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG,"WARN no loc perm granted")

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100)
        } else {
            Log.i(TAG, "loc perm granted!")
            locGranted = true
            testingCode()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            100 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.i(TAG, "SUCCESS client granted perms")
                    locGranted = true
                } else {
                    Log.i(TAG,"FAIL client denied perms")
                }
                return
            }
        }
    }

    // code to test our task service
    fun testingCode() {
        val intent = Intent(this, TaskService::class.java)

        //todo: fetch a location to test with
        val testTask = Task()
        //val locStep = LocStep(null)
        //testTask.steps.add(locStep)

        //todo: make task serializable and put extra in intent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

}
