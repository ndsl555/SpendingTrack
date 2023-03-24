package com.example.inventory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.data.Example2Item
import com.example.inventory.data.ItemRoomDatabase
import kotlinx.coroutines.NonDisposableHandle.parent

class Example2Adapter(private val exampleList: List<Example2Item>) :
    RecyclerView.Adapter<Example2Adapter.ExampleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rank_item,
            parent, false)

        return ExampleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem = exampleList[position]
        holder.textView1.text="第"+(position+1).toString()+"名"
        holder.textView2.text = currentItem.itemname
        holder.textView3.text=currentItem.itemprice.toString()+"元"


    }

    override fun getItemCount() = exampleList.size

    class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView1: TextView = itemView.findViewById(R.id.txtnum)
        val textView2: TextView = itemView.findViewById(R.id.txtname)
        val textView3:TextView=itemView.findViewById(R.id.txtprice)

    }
}