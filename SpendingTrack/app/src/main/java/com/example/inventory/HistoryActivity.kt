package com.example.inventory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.inventory.data.Example1Item
import com.example.inventory.data.ItemRoomDatabase
import com.example.inventory.databinding.ActivityHistoryBinding
import java.util.*


class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getSupportActionBar()?.setTitle("歷史紀錄")

        val back=binding.imageButton
        val foward=binding.imageButton2
        val mon_tv=binding.textView2
        var calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"))
        val thisyear=calendar.get(Calendar.YEAR).toString()
        val thismonth=(calendar.get(Calendar.MONTH)+1).toString()
        val thisday=calendar.get(Calendar.DATE).toString()
        var showmonth=thismonth.toInt()
        mon_tv.setText(showmonth.toString()+"月")

        var totalcost=0
        var pricelist: ArrayList<Double>
        var itemnamelist:ArrayList<String>
        var itemcolorlist:ArrayList<String>
        var perpricelist: ArrayList<Double>
        var peritemnamelist:ArrayList<String>
        var peritemcolorlist:ArrayList<String>
        var yearlist: ArrayList<String>
        var monthlist: ArrayList<String>
        var daylist:ArrayList<String>
        var datelist:ArrayList<String>

        pricelist= ArrayList()
        itemnamelist= ArrayList()
        itemcolorlist= ArrayList()
        perpricelist= ArrayList()
        peritemnamelist= ArrayList()
        peritemcolorlist= ArrayList()
        yearlist= ArrayList()
        monthlist= ArrayList()
        daylist= ArrayList()
        datelist= ArrayList()

        ItemRoomDatabase.getDatabase(this).itemDao().getItems().asLiveData()
            .observe(this, androidx.lifecycle.Observer {
                it.forEach {
                    pricelist.add(it.itemPrice)
                    itemnamelist.add(it.itemName)
                    itemcolorlist.add(it.itemColorcode)
                    yearlist.add(it.itemYear)
                    monthlist.add(it.itemMonth)
                    daylist.add(it.itemDay)
                }
                for (i in 0 until pricelist.size) {
                    if (yearlist.get(i).equals(thisyear) && monthlist.get(i).equals(showmonth.toString())
                    ) {
                        totalcost += pricelist.get(i).toInt()
                        perpricelist.add(pricelist.get(i))
                        peritemnamelist.add(itemnamelist.get(i))
                        peritemcolorlist.add(itemcolorlist.get(i))
                        datelist.add(yearlist.get(i)+'/'+monthlist.get(i)+'/'+daylist.get(i))
                    }
                }
                println(pricelist)
                val list = ArrayList<Example1Item>()
                for (i in 0 until perpricelist.size) {
                    val item = Example1Item(datelist.get(i),peritemnamelist.get(i),peritemcolorlist.get(i),perpricelist.get(i).toInt())
                    list+=item
                }
                binding.perdaycost.setText("共"+totalcost.toString()+"元")
                binding.recyclerView1.addItemDecoration(DividerItemDecoration(this@HistoryActivity, LinearLayoutManager.VERTICAL))
                binding.recyclerView1.adapter=Example1Adapter(list, ItemRoomDatabase.getDatabase(this.applicationContext))
                binding.recyclerView1.layoutManager = LinearLayoutManager(this)
                binding.recyclerView1.setHasFixedSize(true)
            })


        //display(showmonth)
        back.setOnClickListener {
            if (showmonth>1){
                showmonth--
            }
            mon_tv.setText(showmonth.toString()+"月")
            var totalcost=0
            var pricelist: ArrayList<Double>
            var itemnamelist:ArrayList<String>
            var itemcolorlist:ArrayList<String>
            var perpricelist: ArrayList<Double>
            var peritemnamelist:ArrayList<String>
            var peritemcolorlist:ArrayList<String>
            var yearlist: ArrayList<String>
            var monthlist: ArrayList<String>
            var daylist:ArrayList<String>
            var datelist:ArrayList<String>

            pricelist= ArrayList()
            itemnamelist= ArrayList()
            itemcolorlist= ArrayList()
            perpricelist= ArrayList()
            peritemnamelist= ArrayList()
            peritemcolorlist= ArrayList()
            yearlist= ArrayList()
            monthlist= ArrayList()
            daylist= ArrayList()
            datelist= ArrayList()

            ItemRoomDatabase.getDatabase(this).itemDao().getItems().asLiveData()
                .observe(this, androidx.lifecycle.Observer {
                    it.forEach {
                        pricelist.add(it.itemPrice)
                        itemnamelist.add(it.itemName)
                        itemcolorlist.add(it.itemColorcode)
                        yearlist.add(it.itemYear)
                        monthlist.add(it.itemMonth)
                        daylist.add(it.itemDay)
                    }
                    for (i in 0 until pricelist.size) {
                        if (yearlist.get(i).equals(thisyear) && monthlist.get(i).equals(showmonth.toString())
                        ) {
                            totalcost += pricelist.get(i).toInt()
                            perpricelist.add(pricelist.get(i))
                            peritemnamelist.add(itemnamelist.get(i))
                            peritemcolorlist.add(itemcolorlist.get(i))
                            datelist.add(yearlist.get(i)+'/'+monthlist.get(i)+'/'+daylist.get(i))
                        }
                    }
                    println(pricelist)
                    val list = ArrayList<Example1Item>()
                    for (i in 0 until perpricelist.size) {
                        val item = Example1Item(datelist.get(i),peritemnamelist.get(i),peritemcolorlist.get(i),perpricelist.get(i).toInt())
                        list+=item
                    }
                    binding.perdaycost.setText("共"+totalcost.toString()+"元")
                    binding.recyclerView1.addItemDecoration(DividerItemDecoration(this@HistoryActivity, LinearLayoutManager.VERTICAL))
                    binding.recyclerView1.adapter=Example1Adapter(list,ItemRoomDatabase.getDatabase(this.applicationContext))
                    binding.recyclerView1.layoutManager = LinearLayoutManager(this)
                    binding.recyclerView1.setHasFixedSize(true)
                })

        }
        foward.setOnClickListener {
            if (showmonth<12){
                showmonth++
            }
            mon_tv.setText(showmonth.toString()+"月")
            //display(showmonth)
            var totalcost=0
            var pricelist: ArrayList<Double>
            var itemnamelist:ArrayList<String>
            var itemcolorlist:ArrayList<String>
            var perpricelist: ArrayList<Double>
            var peritemnamelist:ArrayList<String>
            var peritemcolorlist:ArrayList<String>
            var yearlist: ArrayList<String>
            var monthlist: ArrayList<String>
            var daylist:ArrayList<String>
            var datelist:ArrayList<String>

            pricelist= ArrayList()
            itemnamelist= ArrayList()
            itemcolorlist= ArrayList()
            perpricelist= ArrayList()
            peritemnamelist= ArrayList()
            peritemcolorlist= ArrayList()
            yearlist= ArrayList()
            monthlist= ArrayList()
            daylist= ArrayList()
            datelist= ArrayList()

            ItemRoomDatabase.getDatabase(this).itemDao().getItems().asLiveData()
                .observe(this, androidx.lifecycle.Observer {
                    it.forEach {
                        pricelist.add(it.itemPrice)
                        itemnamelist.add(it.itemName)
                        itemcolorlist.add(it.itemColorcode)
                        yearlist.add(it.itemYear)
                        monthlist.add(it.itemMonth)
                        daylist.add(it.itemDay)
                    }
                    for (i in 0 until pricelist.size) {
                        if (yearlist.get(i).equals(thisyear) && monthlist.get(i).equals(showmonth.toString())
                        ) {
                            totalcost += pricelist.get(i).toInt()
                            perpricelist.add(pricelist.get(i))
                            peritemnamelist.add(itemnamelist.get(i))

                            peritemcolorlist.add(itemcolorlist.get(i))
                            datelist.add(yearlist.get(i)+'/'+monthlist.get(i)+'/'+daylist.get(i))
                        }
                    }
                    println(pricelist)
                    val list = ArrayList<Example1Item>()
                    for (i in 0 until perpricelist.size) {
                        val item = Example1Item(datelist.get(i),peritemnamelist.get(i),peritemcolorlist.get(i),perpricelist.get(i).toInt())
                        list+=item
                    }
                    binding.perdaycost.setText("共"+totalcost.toString()+"元")
                    binding.recyclerView1.addItemDecoration(DividerItemDecoration(this@HistoryActivity, LinearLayoutManager.VERTICAL))
                    binding.recyclerView1.adapter=Example1Adapter(list, ItemRoomDatabase.getDatabase(this.applicationContext))
                    binding.recyclerView1.layoutManager = LinearLayoutManager(this)
                    binding.recyclerView1.setHasFixedSize(true)
                })
        }
    }

    /*fun display(int: Int) {
        var calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"))
        val thisyear=calendar.get(Calendar.YEAR).toString()
        val thismonth=(calendar.get(Calendar.MONTH)+1).toString()
        val thisday=calendar.get(Calendar.DATE).toString()
        var totalcost=0
        var pricelist: ArrayList<Double>
        var itemnamelist:ArrayList<String>
        var itemcolorlist:ArrayList<String>
        var perpricelist: ArrayList<Double>
        var peritemnamelist:ArrayList<String>
        var peritemcolorlist:ArrayList<String>
        var yearlist: ArrayList<String>
        var monthlist: ArrayList<String>
        var daylist:ArrayList<String>
        var datelist:ArrayList<String>

        pricelist= ArrayList()
        itemnamelist= ArrayList()
        itemcolorlist= ArrayList()
        perpricelist= ArrayList()
        peritemnamelist= ArrayList()
        peritemcolorlist= ArrayList()
        yearlist= ArrayList()
        monthlist= ArrayList()
        daylist= ArrayList()
        datelist= ArrayList()

        ItemRoomDatabase.getDatabase(this).itemDao().getItems().asLiveData()
            .observe(this, androidx.lifecycle.Observer {
            it.forEach {
                pricelist.add(it.itemPrice)
                itemnamelist.add(it.itemName)
                itemcolorlist.add(it.itemColorcode)
                yearlist.add(it.itemYear)
                monthlist.add(it.itemMonth)
                daylist.add(it.itemDay)
            }
            for (i in 0 until pricelist.size) {
                if (yearlist.get(i).equals(thisyear) && monthlist.get(i).equals(int)
                ) {
                    totalcost += pricelist.get(i).toInt()
                    perpricelist.add(pricelist.get(i))
                    peritemnamelist.add(itemnamelist.get(i))
                    peritemcolorlist.add(itemcolorlist.get(i))
                    datelist.add(yearlist.get(i)+'/'+monthlist.get(i)+'/'+daylist.get(i))
                }
            }
            println(pricelist)
            val list = ArrayList<Example1Item>()
            for (i in 0 until perpricelist.size) {
                val item = Example1Item(peritemnamelist.get(i),perpricelist.get(i).toInt(),datelist.get(i),peritemcolorlist.get(i))
                list+=item
            }
            binding.recyclerView1.adapter=Example1Adapter(list)
            binding.recyclerView1.layoutManager = LinearLayoutManager(this)
            binding.recyclerView1.setHasFixedSize(true)
        })

    }*/
}