package com.example.inventory

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.data.Example1Item
import com.example.inventory.data.ItemRoomDatabase


class Example1Adapter(private val exampleList: List<Example1Item>,private val database: ItemRoomDatabase) :
    RecyclerView.Adapter<Example1Adapter.ExampleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.per_month_item,
            parent, false)

        return ExampleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem = exampleList[position]

        holder.textView1.text = currentItem.itemdate
        holder.textView2.text = currentItem.itemname
        holder.textView3.text=currentItem.itemprice.toString()
        holder.textView4.setBackgroundColor(Color.parseColor(currentItem.itemcode))
        holder.imageButton.setOnClickListener {


            AlertDialog.Builder(it.context).apply {
                setTitle("確定要刪除?")
                setMessage("刪除將無法恢復!")
                setPositiveButton("確定") { _: DialogInterface?, _: Int ->
                    AsyncTask.execute(Runnable {
                        database.itemDao().deleteByarg(currentItem.itemname,currentItem.itemprice/1.0
                            ,currentItem.itemcode,currentItem.itemdate.split("/")[0]
                            ,currentItem.itemdate.split("/")[1],currentItem.itemdate.split("/")[2])
                    })
                    Toast.makeText(context, "刪除成功!", Toast.LENGTH_SHORT).show()
                }
                setNegativeButton("取消") { _, _ ->
                    Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show()
                }
            }.create().show()

        }

    }

    override fun getItemCount() = exampleList.size

    class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView1: TextView = itemView.findViewById(R.id.item_permonthdate)
        val textView2: TextView = itemView.findViewById(R.id.item_permonthname)
        val textView3:TextView=itemView.findViewById(R.id.item_permonthprice)
        val textView4:TextView=itemView.findViewById(R.id.item_colorshow)
        val imageButton: ImageButton =itemView.findViewById(R.id.deletebtn)
    }
}