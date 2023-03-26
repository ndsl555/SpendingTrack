package com.example.inventory

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.inventory.data.Example1Item
import com.example.inventory.data.Example2Item
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
                //Ranking
                binding.rankForHistory.setOnClickListener {
                    val dialogBinding = layoutInflater.inflate(R.layout.rankdialog,null)
                    val myDialog = Dialog(this@HistoryActivity)
                    myDialog.setContentView(dialogBinding)
                    myDialog.setCancelable(true)
                    myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    myDialog.show()

                    val ranklist=dialogBinding.findViewById<RecyclerView>(R.id.ranklist)
                    val RankitemList = peritemnamelist.toSet().toMutableList()


                    var RankpriceList: ArrayList<Int>
                    RankpriceList= ArrayList()
                    for (i in 0 until RankitemList.size){
                        var sum = 0
                        for (j in 0 until peritemnamelist.size){
                            if (RankitemList.get(i).equals(peritemnamelist.get(j))){
                                sum+=perpricelist.get(j).toInt()
                            }
                        }
                        RankpriceList.add(sum)
                    }


                    var tempVar:Int
                    var temp:String
                    for (i in 0 until RankpriceList.size) {
                        for (j in 0 until RankpriceList.size-i-1) {
                            if (RankpriceList[j] < RankpriceList[j + 1]) {
                                tempVar = RankpriceList[j + 1]
                                RankpriceList[j + 1] = RankpriceList[j]
                                RankpriceList[j]= tempVar

                                temp=RankitemList[j+1]
                                RankitemList[j+1]=RankitemList[j]
                                RankitemList[j]=temp
                            }
                        }
                    }


                    val list = ArrayList<Example2Item>()

                    for (i in 0 until RankpriceList.size) {
                        val item = Example2Item(RankitemList[i], RankpriceList[i])
                        list+=item
                    }

                    ranklist.addItemDecoration(DividerItemDecoration(this@HistoryActivity, LinearLayoutManager.VERTICAL))
                    ranklist.adapter=Example2Adapter(list)
                    ranklist.layoutManager = LinearLayoutManager(this@HistoryActivity)
                    ranklist.setHasFixedSize(true)

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

                    //Ranking
                    binding.rankForHistory.setOnClickListener {
                        val dialogBinding = layoutInflater.inflate(R.layout.rankdialog,null)
                        val myDialog = Dialog(this@HistoryActivity)
                        myDialog.setContentView(dialogBinding)
                        myDialog.setCancelable(true)
                        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        myDialog.show()

                        val ranklist=dialogBinding.findViewById<RecyclerView>(R.id.ranklist)
                        val RankitemList = peritemnamelist.toSet().toMutableList()


                        var RankpriceList: ArrayList<Int>
                        RankpriceList= ArrayList()
                        for (i in 0 until RankitemList.size){
                            var sum = 0
                            for (j in 0 until peritemnamelist.size){
                                if (RankitemList.get(i).equals(peritemnamelist.get(j))){
                                    sum+=perpricelist.get(j).toInt()
                                }
                            }
                            RankpriceList.add(sum)
                        }


                        var tempVar:Int
                        var temp:String
                        for (i in 0 until RankpriceList.size) {
                            for (j in 0 until RankpriceList.size-i-1) {
                                if (RankpriceList[j] < RankpriceList[j + 1]) {
                                    tempVar = RankpriceList[j + 1]
                                    RankpriceList[j + 1] = RankpriceList[j]
                                    RankpriceList[j]= tempVar

                                    temp=RankitemList[j+1]
                                    RankitemList[j+1]=RankitemList[j]
                                    RankitemList[j]=temp
                                }
                            }
                        }


                        val list = ArrayList<Example2Item>()

                        for (i in 0 until RankpriceList.size) {
                            val item = Example2Item(RankitemList[i], RankpriceList[i])
                            list+=item
                        }

                        ranklist.addItemDecoration(DividerItemDecoration(this@HistoryActivity, LinearLayoutManager.VERTICAL))
                        ranklist.adapter=Example2Adapter(list)
                        ranklist.layoutManager = LinearLayoutManager(this@HistoryActivity)
                        ranklist.setHasFixedSize(true)

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

                    //Ranking
                    binding.rankForHistory.setOnClickListener {
                        val dialogBinding = layoutInflater.inflate(R.layout.rankdialog,null)
                        val myDialog = Dialog(this@HistoryActivity)
                        myDialog.setContentView(dialogBinding)
                        myDialog.setCancelable(true)
                        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        myDialog.show()

                        val ranklist=dialogBinding.findViewById<RecyclerView>(R.id.ranklist)
                        val RankitemList = peritemnamelist.toSet().toMutableList()


                        var RankpriceList: ArrayList<Int>
                        RankpriceList= ArrayList()
                        for (i in 0 until RankitemList.size){
                            var sum = 0
                            for (j in 0 until peritemnamelist.size){
                                if (RankitemList.get(i).equals(peritemnamelist.get(j))){
                                    sum+=perpricelist.get(j).toInt()
                                }
                            }
                            RankpriceList.add(sum)
                        }


                        var tempVar:Int
                        var temp:String
                        for (i in 0 until RankpriceList.size) {
                            for (j in 0 until RankpriceList.size-i-1) {
                                if (RankpriceList[j] < RankpriceList[j + 1]) {
                                    tempVar = RankpriceList[j + 1]
                                    RankpriceList[j + 1] = RankpriceList[j]
                                    RankpriceList[j]= tempVar

                                    temp=RankitemList[j+1]
                                    RankitemList[j+1]=RankitemList[j]
                                    RankitemList[j]=temp
                                }
                            }
                        }


                        val list = ArrayList<Example2Item>()

                        for (i in 0 until RankpriceList.size) {
                            val item = Example2Item(RankitemList[i], RankpriceList[i])
                            list+=item
                        }

                        ranklist.addItemDecoration(DividerItemDecoration(this@HistoryActivity, LinearLayoutManager.VERTICAL))
                        ranklist.adapter=Example2Adapter(list)
                        ranklist.layoutManager = LinearLayoutManager(this@HistoryActivity)
                        ranklist.setHasFixedSize(true)

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

}
