package com.spencerbyson.gpstasks

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class MainRecyclerAdapter(val userList: ArrayList<String>): RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.left?.text = userList[position] + "0"
        holder?.middle?.text = userList[position]  + "1"
        holder?.right?.text = userList[position] + "2"
        holder.itemView.setOnClickListener{
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(parent?.context).inflate(R.layout.card_main_recyclerview, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun deleteItem(i: Int){
        userList.removeAt(i)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val left = itemView.findViewById<TextView>(R.id.mLeftText)
        val middle = itemView.findViewById<TextView>(R.id.mMiddleText)
        val right = itemView.findViewById<TextView>(R.id.mRightText)
    }


}

