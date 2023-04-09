package com.example.inventory

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.data.Example3Item
import com.example.inventory.data.ItemRoomDatabase

class Example3Adapter(private val exampleList: List<Example3Item>, private val database: ItemRoomDatabase,
      private val monlist:ArrayList<String>, private val targetlist:ArrayList<String>) :
    RecyclerView.Adapter<Example3Adapter.ExampleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.per_invoice_item,
            parent, false)

        return ExampleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem = exampleList[position]
        holder.textView1.text = currentItem.itemdate
        holder.textView2.text = currentItem.itemnumber
        //holder.textView3.text=monlist[0]+'-'+monlist[1]+'月'

        if (currentItem.itemdate.split('/')[1].toInt().equals(monlist[0].toInt())
            ||currentItem.itemdate.split('/')[1].toInt().equals(monlist[1].toInt())){
            if (currentItem.itemnumber == targetlist[0]) {
                holder.textView3.text=monlist[0]+'-'+monlist[1]+'月'
                holder.textView4.text="特別獎:一千萬元!"
            } else if (currentItem.itemnumber == targetlist[1]) {
                holder.textView3.text=monlist[0]+'-'+monlist[1]+'月'
                holder.textView4.text="特獎:兩百萬元!"
            } else if (currentItem.itemnumber == targetlist[2] || currentItem.itemnumber == targetlist[3] || currentItem.itemnumber == targetlist[4]) {
                holder.textView3.text=monlist[0]+'-'+monlist[1]+'月'
                holder.textView4.text="頭獎:二十萬元!"
            } else {
                if (currentItem.itemnumber.substring(5) == targetlist[2].substring(5) || currentItem.itemnumber.substring(5) == targetlist[3].substring(
                        5
                    ) || currentItem.itemnumber.substring(5) == targetlist[4].substring(5)
                ) {
                    holder.textView3.text=monlist[0]+'-'+monlist[1]+'月'
                    holder.textView4.text="200元"
                } else if (currentItem.itemnumber.substring(4) == targetlist[2].substring(4) || currentItem.itemnumber.substring(4) == targetlist[3].substring(
                        4
                    ) || currentItem.itemnumber.substring(4) == targetlist[4].substring(4)
                ) {
                    holder.textView3.text=monlist[0]+'-'+monlist[1]+'月'
                    holder.textView4.text="1000元"
                } else if (currentItem.itemnumber.substring(3) == targetlist[2].substring(3) || currentItem.itemnumber.substring(3) == targetlist[3].substring(
                        3
                    ) || currentItem.itemnumber.substring(3) == targetlist[4].substring(3)
                ) {
                    holder.textView3.text=monlist[0]+'-'+monlist[1]+'月'
                    holder.textView4.text="4000元"
                } else if (currentItem.itemnumber.substring(2) == targetlist[2].substring(2) || currentItem.itemnumber.substring(2) == targetlist[3].substring(
                        2
                    ) || currentItem.itemnumber.substring(2) == targetlist[4].substring(2)
                ) {
                    holder.textView3.text=monlist[0]+'-'+monlist[1]+'月'
                    holder.textView4.text="一萬元"
                } else if (currentItem.itemnumber.substring(1) == targetlist[2].substring(1) || currentItem.itemnumber.substring(1) == targetlist[3].substring(
                        1
                    ) || currentItem.itemnumber.substring(1) == targetlist[4].substring(1)
                ) {
                    holder.textView3.text=monlist[0]+'-'+monlist[1]+'月'
                    holder.textView4.text="四萬元"
                } else {
                    //holder.textView3.text=monlist[0]+'-'+monlist[1]+'月'
                    //alertTextView.setText("槓龜,再接再厲...")
                }
            }
        }
        else if (currentItem.itemdate.split('/')[1].toInt().equals(monlist[2].toInt())
            ||currentItem.itemdate.split('/')[1].toInt().equals(monlist[3].toInt())){
            if (currentItem.itemnumber == targetlist[5]) {
                holder.textView3.text=monlist[2]+'-'+monlist[3]+'月'
                holder.textView4.text="特別獎:一千萬元!"
            } else if (currentItem.itemnumber == targetlist[6]) {
                holder.textView3.text=monlist[2]+'-'+monlist[3]+'月'
                holder.textView4.text="特獎:兩百萬元!"
            } else if (currentItem.itemnumber == targetlist[7] || currentItem.itemnumber == targetlist[8] || currentItem.itemnumber == targetlist[9]) {
                holder.textView3.text=monlist[2]+'-'+monlist[3]+'月'
                holder.textView4.text="頭獎:二十萬元!"
            } else {
                if (currentItem.itemnumber.substring(5) == targetlist[7].substring(5) || currentItem.itemnumber.substring(5) == targetlist[8].substring(
                        5
                    ) || currentItem.itemnumber.substring(5) == targetlist[9].substring(5)
                ) {
                    holder.textView3.text=targetlist[2]+'-'+targetlist[3]+'月'
                    holder.textView4.text="200元"
                } else if (currentItem.itemnumber.substring(4) == targetlist[7].substring(4) || currentItem.itemnumber.substring(4) == targetlist[8].substring(
                        4
                    ) || currentItem.itemnumber.substring(4) == targetlist[9].substring(4)
                ) {
                    holder.textView3.text=monlist[2]+'-'+monlist[3]+'月'
                    holder.textView4.text="1000元"
                } else if (currentItem.itemnumber.substring(3) == targetlist[7].substring(3) || currentItem.itemnumber.substring(3) == targetlist[8].substring(
                        3
                    ) || currentItem.itemnumber.substring(3) == targetlist[9].substring(3)
                ) {
                    holder.textView3.text=monlist[2]+'-'+monlist[3]+'月'
                    holder.textView4.text="4000元"
                } else if (currentItem.itemnumber.substring(2) == targetlist[7].substring(2) || currentItem.itemnumber.substring(2) == targetlist[8].substring(
                        2
                    ) || currentItem.itemnumber.substring(2) == targetlist[9].substring(2)
                ) {
                    holder.textView3.text=monlist[2]+'-'+monlist[3]+'月'
                    holder.textView4.text="一萬元"
                } else if (currentItem.itemnumber.substring(1) == targetlist[7].substring(1) || currentItem.itemnumber.substring(1) == targetlist[8].substring(
                        1
                    ) || currentItem.itemnumber.substring(1) == targetlist[9].substring(1)
                ) {
                    holder.textView3.text=monlist[2]+'-'+monlist[3]+'月'
                    holder.textView4.text="四萬元"
                } else {
                    //holder.textView3.text=monlist[2]+'-'+monlist[3]+'月'
                    //alertTextView.setText("槓龜,再接再厲...")
                }
            }
        }

        holder.imageButton.setOnClickListener {
            AlertDialog.Builder(it.context).apply {
                setTitle("確定要刪除?")
                setMessage("刪除將無法恢復!")
                setPositiveButton("確定") { _: DialogInterface?, _: Int ->
                    AsyncTask.execute(Runnable {
                        database.InvoiceDao().deleteByarg(currentItem.itemdate,currentItem.itemnumber)
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
        val textView1: TextView = itemView.findViewById(R.id.item_date)
        val textView2: TextView = itemView.findViewById(R.id.item_invoicenum)
        val textView3: TextView =itemView.findViewById(R.id.item_month)
        val textView4: TextView =itemView.findViewById(R.id.item_prizeshow)
        val imageButton: ImageButton =itemView.findViewById(R.id.delete_invoice_btn)
    }
}