package com.spencerbyson.gpstasks

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView

class TaskBuilderRecyclerAdapter(val taskList: ArrayList<Step>): RecyclerView.Adapter<TaskBuilderRecyclerAdapter.ViewHolder>() {

    var listener: PreviewListener? = null

    interface PreviewListener {
        fun startMapPreview()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val step = taskList[position]
        holder.title.text = step.type.toString()
        holder.preview.setOnClickListener {
            listener?.startMapPreview()
        }

//        holder.itemView.setOnClickListener{
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(parent?.context).inflate(R.layout.card_taskbuilder_recyclerview, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun deleteItem(i: Int){
        taskList.removeAt(i)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var title = itemView.findViewById<TextView>(R.id.mStepType)
        var preview = itemView.findViewById<TextView>(R.id.mPreview)
    }


}