package com.example.gestureapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestureapp.activities.StopWatchActivity
import com.example.gestureapp.activities.TimerActivity
import com.example.gestureapp.activities.UseActivity
import com.example.gestureapp.activities.laps.LapsActivity
import com.example.gestureapp.gestures.Gestures

class MainAdapter(private val sensorLogic: Gestures) : RecyclerView.Adapter<MainAdapter.MyViewHolder>() {

    private val dataList = listOf("stop watch", "timer", "laps", "how to use")
    private val iconList = listOf(R.drawable.stop_watch, R.drawable.timer, R.drawable.laps, R.drawable.use)

    class MyViewHolder(view: View, private val sensorLogic: Gestures) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.main_item_button)
        private val activityMap = mapOf(
            "stop watch" to StopWatchActivity::class.java,
            "timer" to TimerActivity::class.java,
            "laps" to LapsActivity::class.java,
            "how to use" to UseActivity::class.java
        )
        init {
            textView.setOnClickListener { it ->
                sensorLogic.stopSensors()
                val context = itemView.context
                val text = (it as Button).text
                activityMap[text]?.let {
                    val intent = Intent(context, it)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_list_item, parent, false)
        return MyViewHolder(view, sensorLogic)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = dataList[position]
        val icon = iconList.getOrNull(position)
        holder.textView.setCompoundDrawablesWithIntrinsicBounds(icon ?: 0, 0, 0, 0)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}