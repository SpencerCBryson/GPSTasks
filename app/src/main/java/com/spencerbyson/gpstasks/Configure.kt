package com.spencerbyson.gpstasks

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class Configure : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configure)

        //TODO dynamically design screen based on this extra.


        if(intent.getStringExtra("Task") == "Enter Area"){
            Log.d("nice", "Enter Area Mode")

        }

    }
}
