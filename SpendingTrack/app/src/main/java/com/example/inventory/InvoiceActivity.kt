package com.example.inventory

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventory.data.Example2Item
import com.example.inventory.data.Example3Item
import com.example.inventory.data.ItemRoomDatabase
import com.example.inventory.databinding.ActivityInvoiceBinding
import com.example.inventory.databinding.ActivityQrmainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InvoiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInvoiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getSupportActionBar()?.setTitle("發票倉庫")
        val monlist=intent.getStringArrayListExtra("month")!!
        val targetlist=intent.getStringArrayListExtra("target")!!
        var invoicelist:ArrayList<String>
        var datelist:ArrayList<String>
        invoicelist= ArrayList()
        datelist= ArrayList()
        println(monlist)
        for (i in 0 until monlist.size){
            if (monlist[i].toInt()<=0){
                monlist[i]=(monlist[i].toInt()+12).toString()
            }
        }
        binding.floatingdelAllButton.setOnClickListener {

            AlertDialog.Builder(it.context).apply {
                setTitle("確定要刪除所有紀錄?")
                setMessage("刪除將無法恢復!")
                setPositiveButton("確定") { _: DialogInterface?, _: Int ->
                    AsyncTask.execute(Runnable {
                        GlobalScope.launch(Dispatchers.IO){
                            ItemRoomDatabase.getDatabase(applicationContext).InvoiceDao().deleteAll()
                        }
                    })
                    Toast.makeText(context, "刪除成功!", Toast.LENGTH_SHORT).show()
                }
                setNegativeButton("取消") { _, _ ->
                    Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show()
                }
            }.create().show()
        }
        println(monlist)
        ItemRoomDatabase.getDatabase(applicationContext).InvoiceDao().getItems().asLiveData()
            .observe(this,androidx.lifecycle.Observer{
                it.forEach {
                    invoicelist.add(it.itemnumber)
                    datelist.add(it.itemdate)
                }

                val list = ArrayList<Example3Item>()

                for (i in 0 until invoicelist.size) {
                    if (datelist[i].split('/')[1].toInt()>monlist[0].toInt()-2){
                        val item = Example3Item(datelist[i], invoicelist[i])
                        list+=item
                    }
                }

                binding.invoiceRecycle.addItemDecoration(DividerItemDecoration(this@InvoiceActivity, LinearLayoutManager.VERTICAL))
                binding.invoiceRecycle.adapter=Example3Adapter(list, ItemRoomDatabase.getDatabase(this.applicationContext),monlist,targetlist)
                binding.invoiceRecycle.layoutManager = LinearLayoutManager(this)
                binding.invoiceRecycle.setHasFixedSize(true)
            }
            )
    //binding.textView.text=ttt?.get(0)

    }
}