package com.spencerbyson.gpstasks

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AddTask : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val addButton = findViewById<Button>(R.id.mAddButton)

        addButton.setOnClickListener {
            val intent = Intent(this, AddSubtask::class.java)
            startActivityForResult(intent, 2)
        }
    }
}
