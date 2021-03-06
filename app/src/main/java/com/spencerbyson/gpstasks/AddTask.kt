package com.spencerbyson.gpstasks

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.sms_action.*

class AddTask : AppCompatActivity(), TaskBuilderRecyclerAdapter.PreviewListener, TextListener {

    val TAG = "GPSTasks"

    var editMode = false
    var oldTask : Task? = null

    override fun startMapPreview() {
        val fm = supportFragmentManager.beginTransaction()
        mapFragment = MapPreview()
        System.out.println("hurrah")

        fm.add(mapFragment!!, "map")
            .commit()
    }

    val CREATE_LOCATION_TRIGGER = 0

    lateinit var rv : RecyclerView
    val currentSteps : ArrayList<Step> = ArrayList()

    var selectionFragment : Fragment? = null
    var mapFragment : Fragment? = null

    override fun optionSelected(view : View, text : String, phoneNumber : String) {
        val action = SMSAction(phoneNumber, text)
        supportFragmentManager.beginTransaction()
            .remove(selectionFragment!!)
            .commit()
        currentSteps.add(action)
    }

    fun optionSelected(view : View) {
        when (view.id) {
            R.id.enter_area -> {
                supportFragmentManager.beginTransaction()
                    .remove(selectionFragment!!)
                    .commit()
                val intent = Intent(this, ConfigureTaskLocation::class.java)
                startActivityForResult(intent, CREATE_LOCATION_TRIGGER)
            }
            R.id.send_SMS -> {
                val fm = supportFragmentManager.beginTransaction()
                    .remove(selectionFragment!!)
                selectionFragment = SMSActionFragment.newInstance(this)

                fm.add(selectionFragment!!, "SMS")
                    .commit()
            }
            R.id.play_music -> {
                supportFragmentManager.beginTransaction()
                    .remove(selectionFragment!!)
                    .commit()
                currentSteps.add(AudioAction(view.context))
                rv.adapter!!.notifyItemInserted(currentSteps.size)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CREATE_LOCATION_TRIGGER && resultCode == 1) {
            val newStep = data!!.getParcelableExtra<LocStep>("step")
            currentSteps.add(newStep)
            rv.adapter!!.notifyItemInserted(currentSteps.size)
        }
    }

    fun finishTaskBuilder(view : View) {
        val title = findViewById<TextView>(R.id.mtaskNameTxt).text.toString()
        val task = Task(title, currentSteps, true)
        val data = Intent()

        data.putExtra("task", task)

        Log.i(TAG, editMode.toString())

        if(editMode) {
            data.putExtra("oldTitle", oldTask!!.title)
            setResult(2, data)
        } else {
            setResult(1, data)
        }
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val addButton = findViewById<Button>(R.id.mAddButton)

//        val contextMenu = findViewById<Menu>(addButton)

        editMode = intent.getBooleanExtra("editMode", false)

        if(editMode) {
            val task = intent.getParcelableExtra<Task>("task")

            Log.i(TAG, task.steps.size.toString())

            task.steps.forEach { currentSteps.add(it)}

            oldTask = task
        }

        rv = findViewById(R.id.mTaskBuilderRecyclerView)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val adapter = TaskBuilderRecyclerAdapter(currentSteps)
        adapter.listener = this
        rv.adapter = adapter

        addButton.setOnClickListener {
//            it.showContextMenu()
            val fm = supportFragmentManager.beginTransaction()
            selectionFragment = AddTaskOrAction.newInstance()

            fm.add(selectionFragment!!, "map")
                .commit()
        }
    }
}
