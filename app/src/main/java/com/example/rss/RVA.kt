package com.example.rss

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RVAdapter(private var arr: ArrayList<StackoverflowData>) :
    RecyclerView.Adapter<RVAdapter.ViewHolder>(){

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView)
    // for binding
    lateinit var title : TextView
    lateinit var des : TextView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val msg = arr[position].name
        holder.itemView.apply {
            title = findViewById(R.id.textview)
            title.text = msg
        }
    }

    override fun getItemCount() = arr.size

}