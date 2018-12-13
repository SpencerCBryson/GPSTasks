package com.spencerbyson.gpstasks

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationCompat.PRIORITY_MIN
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.widget.Toast

class TaskService : Service(), TaskHandler {

    val TAG = "GPSTasks-TaskService"

    lateinit var locManager : LocationManager
    lateinit var tasks : ArrayList<Task>
    lateinit var broadcaster : LocalBroadcastManager

    // sends new location to all tasks
    fun updateLocation(loc : Location) {
        tasks.forEach {
            if(it.enabled) it.updateLocation(loc)
        }
    }

    override fun updateUI(taskId : Int) {
        //TODO: update status of task/steps
    }

    fun updateTask(position : Int, task : Task) {
        tasks[position] = task
    }

    // location listener that calls update location with a new location when available
    val locListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            updateLocation(location!!)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        startForeground()
        setupLocListener()
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int) : Int {
        tasks = intent.extras!!.get("TASKS") as ArrayList<Task>
        tasks.forEach{ task ->
            task.handler = this
        }
        return START_STICKY
    }

    fun setupLocListener() {
        locManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE

        val provider = locManager.getBestProvider(criteria, true)

        try {
            locManager.requestLocationUpdates(provider, 5000, 5.0f, locListener)
            Log.i(TAG, "setup listener")
        } catch (ex : SecurityException) {
            Log.i(TAG, "security exception")
        } catch (ex : IllegalArgumentException) {
            Log.i(TAG, "arg exception")
        }
    }

    override fun onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
        Log.i(TAG, "service done")
    }

    private fun startForeground() {
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel()
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId )
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(PRIORITY_MIN)
            .setContentTitle("GPS Tasks")
            .setContentText("Doing some work...")
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(101, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String{
        val channelId = "task_service"
        val channelName = "Task Service"
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_HIGH)
        chan.lightColor = Color.BLUE
        chan.importance = NotificationManager.IMPORTANCE_NONE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

}