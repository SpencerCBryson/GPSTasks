package com.spencerbyson.gpstasks

import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView


class MainRecyclerAdapter(val taskList: ArrayList<Task>): RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>() {

    val TAG = "GPSTasks-MRA"

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]
        holder.title.text = task.title
        holder.toggle.isChecked = task.enabled

        Log.i(TAG, task.steps.size.toString())

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, AddTask::class.java)
            intent.putExtra("editMode", true)
            intent.putExtra("task", task)
            startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(parent?.context).inflate(R.layout.card_main_recyclerview, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var title = itemView.findViewById<TextView>(R.id.mTitle)
        var toggle = itemView.findViewById<Switch>(R.id.mToggle)
    }
}

