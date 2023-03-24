package com.example.inventory

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.data.Example2Item
import com.example.inventory.data.ExampleItem
import com.example.inventory.data.ItemDao
import com.example.inventory.data.ItemRoomDatabase
import com.example.inventory.databinding.FragmentStripBinding
import java.time.YearMonth
import java.util.*
import java.util.stream.Collectors


class StripFragment : Fragment() {
    private var _binding: FragmentStripBinding?=null
    private val binding get() = _binding!!
    var calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"))
    val thisyear=calendar.get(Calendar.YEAR).toString()
    val thismonth=(calendar.get(Calendar.MONTH)+1).toString()
    val thisday=calendar.get(Calendar.DATE).toString()

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //println(calendar.get(Calendar.DAY_OF_MONTH)-calendar.get(Calendar.DATE))
        _binding = FragmentStripBinding.inflate(inflater, container, false)
        binding.monthnameTv.setText(thismonth+"月總花費")
        //binding.totalcostTv
        ItemRoomDatabase.getDatabase(requireContext()).RuleDao().getLastRule().asLiveData().observe(this.viewLifecycleOwner){ items ->
            items.let {
                if(!it.isNullOrEmpty()){
                    binding.budTv.setText("預算"+it.last().itemRule.toString()+"元")
                    viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
                        var cost = 0.0
                        var pricelist: ArrayList<Double>
                        var perpricelist: ArrayList<Double>
                        var yearlist: ArrayList<String>
                        var monthlist: ArrayList<String>
                        var itemnamelist:ArrayList<String>
                        var peritemnamelist:ArrayList<String>
                        itemnamelist= ArrayList()
                        pricelist = ArrayList()
                        perpricelist= ArrayList()
                        peritemnamelist= ArrayList()
                        yearlist = ArrayList()
                        monthlist = ArrayList()
                        items.let {
                            it.forEach {
                                itemnamelist.add(it.itemName)
                                pricelist.add(it.itemPrice)
                                yearlist.add(it.itemYear)
                                monthlist.add(it.itemMonth)
                            }

                        }
                        if (!pricelist.isNullOrEmpty()){
                                println(pricelist)
                            println("_________")
                            for (i in 0 until pricelist.size) {
                                if (yearlist.get(i).equals(thisyear) && monthlist.get(i)
                                        .equals(thismonth)
                                ) {
                                    peritemnamelist.add(itemnamelist.get(i))
                                    perpricelist.add(pricelist.get(i))
                                    cost += pricelist.get(i)
                                }
                            }
                            binding.rank.setOnClickListener {
                                val dialogBinding = layoutInflater.inflate(R.layout.rankdialog,null)
                                val myDialog = Dialog(requireView().context)
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

                                ranklist.addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL))
                                ranklist.adapter=Example2Adapter(list)
                                ranklist.layoutManager = LinearLayoutManager(this.context)
                                ranklist.setHasFixedSize(true)


                            }
                            binding.progressBar.max=it.last().itemRule
                            binding.progressBar.setProgress(cost.toInt())
                            binding.totalcostTv.setText(cost.toString() + "元")
                            var balance = it.last().itemRule / 1.0 - cost
                            if ((balance) >= 0) {
                                if (balance<1000){
                                    binding.progressBar.progressTintList=ColorStateList.valueOf(Color.parseColor("#FFA500"))
                                }
                                binding.situationTv.setText("剩餘 " + balance.toString() + "元")
                                binding.situationTv.setTextColor(Color.GREEN)
                            }
                            else {
                                binding.progressBar.progressTintList=ColorStateList.valueOf(Color.RED)
                                balance *= -1
                                binding.situationTv.setText("超支 " + balance.toString() + "元")
                                binding.situationTv.setTextColor(Color.RED)
                            }
                        }
                    }
                }

            }
        }
        println(calendar.get(Calendar.DAY_OF_MONTH))
        val daypass= YearMonth.now().lengthOfMonth()-calendar.get(Calendar.DAY_OF_MONTH)+1
        binding.leastdayTv.setText("距離下個月還有"+daypass.toString()+"天")
        //binding.leastdayTv.setText(null)

        return binding.root


        // Inflate the layout for this fragment

    }


}