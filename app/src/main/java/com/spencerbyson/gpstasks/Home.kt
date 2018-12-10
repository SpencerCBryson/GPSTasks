package com.spencerbyson.gpstasks

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
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

    var permsGranted = false
    val TAG = "GPSTasks-Main"

    lateinit var rv : RecyclerView
    var taskList = ArrayList<Task>()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("nice", requestCode.toString() + resultCode.toString() + data.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        var db = DBHelper(this)
        db.populate()
        Log.d("nice", db.readTasks().toString())
        Log.d("nice", db.readSteps().toString())
        Log.d("nice", db.readStepTasks().toString())


        // get permissions, all are required to make the app function correctly
        getPerms()

        rv = findViewById(R.id.mRecyclerView)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rv.adapter = MainRecyclerAdapter(taskList)

        val addButton = findViewById<Button>(R.id.mAddButton)
        addButton.setOnClickListener{
            val intent = Intent(this, AddTask::class.java)
            startActivityForResult(intent, 1)
        }


        if(permsGranted)
            testingCode()
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
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
            (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)) {

            Log.i(TAG, "[WARN] Requesting missing permissions")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS),
                100
            )
        } else {
            Log.i(TAG, "[SUCCESS] Permissions granted")
            permsGranted = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            100 -> {
                if ((grantResults.isNotEmpty()) && (grantResults[0] == PackageManager.PERMISSION_GRANTED) &&
                    (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                        Log.i(TAG, "[SUCCESS] Permissions granted")
                        permsGranted = true
                } else {
                    Log.i(TAG,"[FAIL] Client denied permissions")
                }
                return
            }
        }
    }

    // code to test our task service
    fun testingCode() {
        val intent = Intent(this, TaskService::class.java)

        // test lat & long
        val lat = 43.945302
        val long = -78.892388
        val radius = 100.0 //metres

        val testLoc = Location("")
        testLoc.latitude = lat
        testLoc.longitude = long

        val steps = ArrayList<Step>()

        val number = "+12898032117" //burner phone number from TextNow
        val msg = "Turning into the school now, be there in a couple minutes."
        val smsAction = SMSAction(number, msg)

        val locStep = LocStep(smsAction, testLoc, radius, true)
        steps.add(locStep)

        val testTask = Task("Testing task", steps, true)

        addTask(testTask)

        intent.putExtra("TASKS", taskList)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }

    }

    fun addTask(task : Task) {
        taskList.add(taskList.size, task)
        rv.adapter!!.notifyItemInserted(taskList.size)
    }

}
