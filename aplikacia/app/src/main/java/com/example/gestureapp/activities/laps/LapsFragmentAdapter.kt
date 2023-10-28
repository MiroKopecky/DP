package com.example.gestureapp.activities.laps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestureapp.R


class LapsFragmentAdapter(private val lapTimes: List<String>) : RecyclerView.Adapter<LapsFragmentAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.laps_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = (position + 1).toString() + ". " + lapTimes[position]
    }

    override fun getItemCount(): Int {
        return lapTimes.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.laps_item)
    }

}