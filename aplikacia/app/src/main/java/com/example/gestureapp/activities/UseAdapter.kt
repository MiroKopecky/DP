package com.example.gestureapp.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gestureapp.R

class UseAdapter() : RecyclerView.Adapter<UseAdapter.MyViewHolder>() {
    private val gifs = listOf(
        R.raw.flickup,
        R.raw.flickdown,
        R.raw.up,
        R.raw.down,
        R.raw.right,
        R.raw.left,
        R.raw.dismiss
    )

    private val texts = listOf(
        "TOP button or\nscroll DOWN or RIGHT",
        "BOTTOM button or\nscroll TOP or LEFT",
        "to SELECT something",
        "to go BACK",
        "RIGHT button",
        "LEFT button",
        "to EXIT app"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.use_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(holder.imageView.context)
            .asGif()
            .load(gifs[position])
            .into(holder.imageView)
        holder.textView.text = texts[position]
    }

    override fun getItemCount(): Int {
        return gifs.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.use_item_image)
        val textView: TextView = itemView.findViewById(R.id.use_item_text)
    }

}