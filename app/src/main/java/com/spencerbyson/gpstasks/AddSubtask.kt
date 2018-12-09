package com.spencerbyson.gpstasks

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

class AddSubtask : AppCompatActivity() {

    //TODO Turn the tasks into real tasks, probably change these arrays into objects with tostrings so we can pass them without hardcoding?
    val tasks = arrayOf("Enter Area", "Reach Altitude", "Exit Area")
    val actions = arrayOf("Send Text", "Play Media", "Download Content")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_subtask)

        val task = findViewById<Spinner>(R.id.mSpinnerTask)
        val action = findViewById<Spinner>(R.id.mSpinnerAction)

        val aa1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, tasks)
        task.adapter = aa1

        val aa2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, actions)
        action.adapter = aa2

        val configTaskButton = findViewById<Button>(R.id.mConfigureTaskButton)
        val configActButton = findViewById<Button>(R.id.mConfigureActionButton)

        configTaskButton.setOnClickListener{
            val intent = Intent(this, Configure::class.java)
            intent.putExtra("Task", task.selectedItemPosition.toString())
        }

        configActButton.setOnClickListener{

        }


    }
}
