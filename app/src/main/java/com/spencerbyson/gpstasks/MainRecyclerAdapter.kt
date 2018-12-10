package com.spencerbyson.gpstasks

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView


class MainRecyclerAdapter(val taskList: ArrayList<Task>): RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]
        holder.title.text = task.title
        holder.toggle.isChecked = task.enabled

//        holder.itemView.setOnClickListener{
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(parent?.context).inflate(R.layout.card_main_recyclerview, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun deleteItem(i: Int){
        taskList.removeAt(i)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var title = itemView.findViewById<TextView>(R.id.mTitle)
        var toggle = itemView.findViewById<Switch>(R.id.mToggle)
    }


}

