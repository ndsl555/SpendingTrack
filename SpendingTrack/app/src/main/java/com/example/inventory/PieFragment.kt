package com.example.inventory

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventory.data.ExampleItem
import com.example.inventory.databinding.FragmentPieBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import java.util.*
import java.util.stream.Collectors


class PieFragment : Fragment() {

    private var _binding: FragmentPieBinding?=null
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPieBinding.inflate(inflater, container, false)
        val pieChart=binding.pcChart
        val recyclerView=binding.recyclerView
        binding.history.setOnClickListener {
            activity?.let{
                val intent = Intent (it,HistoryActivity::class.java)
                it.startActivity(intent)
            }

        }

        viewModel.allItems.observe(this.viewLifecycleOwner){
            items->
            val entries = ArrayList<PieEntry>()
            val colors = ArrayList<Int>()
            entries.clear()
            var totalcost=0
            var todaycost=0
            var pricelist: ArrayList<Double>

            var todaypricelist: ArrayList<Double>
            var todayitemlist:ArrayList<String>
            var itemnamelist:ArrayList<String>

            var itemcolorlist:ArrayList<String>

            var yearlist: ArrayList<String>
            var monthlist: ArrayList<String>
            var daylist:ArrayList<String>
            pricelist= ArrayList()

            todaypricelist= ArrayList()
            itemnamelist= ArrayList()

            todayitemlist=ArrayList()
            itemcolorlist= ArrayList()

            yearlist= ArrayList()
            monthlist= ArrayList()
            daylist= ArrayList()
            pieChart.setUsePercentValues(true); //设置是否显示数据实体(百分比，true:以下属性才有意义)
            pieChart.getDescription().setEnabled(false);//设置pieChart图表的描述
            pieChart.setExtraOffsets(5F, 5F, 5F, 5F);//饼形图上下左右边距

            pieChart.setDragDecelerationFrictionCoef(0.95f);//设置pieChart图表转动阻力摩擦系数[0,1]

            pieChart.setDrawHoleEnabled(true);//是否显示PieChart内部圆环(true:下面属性才有意义)
            pieChart.setHoleColor(Color.WHITE);//设置PieChart内部圆的颜色

            pieChart.setTransparentCircleColor(Color.WHITE);//设置PieChart内部透明圆与内部圆间距(31f-28f)填充颜色
            pieChart.setTransparentCircleAlpha(0);//设置PieChart内部透明圆与内部圆间距(31f-28f)透明度[0~255]数值越小越透明
            pieChart.setHoleRadius(30f);//设置PieChart内部圆的半径(这里设置0f,即不要内部圆)
            pieChart.setTransparentCircleRadius(10f);//设置PieChart内部透明圆的半径(这里设置31.0f)

            pieChart.setDrawCenterText(true);//是否绘制PieChart内部中心文本（true：下面属性才有意义）

            pieChart.setRotationAngle(0F);//设置pieChart图表起始角度

            pieChart.setRotationEnabled(true);//设置pieChart图表是否可以手动旋转
            pieChart.setHighlightPerTapEnabled(true);//设置piecahrt图表点击Item高亮是否可用

            pieChart.animateY(1400, Easing.EaseInOutQuad);// 设置pieChart图表展示动画效果

            items.let {
                it.forEach {
                    pricelist.add(it.itemPrice)
                    itemnamelist.add(it.itemName)
                    itemcolorlist.add(it.itemColorcode)
                    yearlist.add(it.itemYear)
                    monthlist.add(it.itemMonth)
                    daylist.add(it.itemDay)
                }
            }
            for (i in 0 until pricelist.size) {
                if (yearlist.get(i).equals(thisyear) && monthlist.get(i)
                        .equals(thismonth)&&daylist.get(i).equals(thisday)
                ) {
                    totalcost += pricelist.get(i).toInt()
                    entries.add(PieEntry(pricelist.get(i).toFloat(),itemnamelist.get(i)))
                    colors.add(Color.parseColor(itemcolorlist.get(i)))
                }
                if (yearlist.get(i).equals(thisyear) && monthlist.get(i)
                        .equals(thismonth)&&daylist.get(i).equals(thisday)
                ) {
                  todaycost+=pricelist.get(i).toInt()
                  todaypricelist.add(pricelist.get(i))
                  todayitemlist.add(itemnamelist.get(i))
                }
            }


           // val listWithoutDuplicates: List<String> =
           //     p_itemnamelist.stream().distinct().collect(Collectors.toList())


            /*for (i in 0 until listWithoutDuplicates.size){
                for (j in 0 until p_itemnamelist.size){
                    if (listWithoutDuplicates.get(i).equals(p_itemnamelist.get(j))){

                    }
                }
            }*/

            //pieChart.setCenterText("$"+todaycost.toString())
            pieChart.setCenterText("$"+totalcost.toString())

            pieChart.setTransparentCircleAlpha(110);
            var pieDataSet = PieDataSet(entries,"");
            pieDataSet.setSliceSpace(3f);           //设置饼状Item之间的间隙
            pieDataSet.setSelectionShift(10f);      //设置饼状Item被选中时变化的距离
            pieDataSet.setColors(colors);           //为DataSet中的数据匹配上颜色集(饼图Item颜色)
            //最终数据 PieData
            var pieData = PieData(pieDataSet);
            pieData.setDrawValues(true);            //设置是否显示数据实体(百分比，true:以下属性才有意义)
            pieData.setValueTextColor(Color.BLUE);  //设置所有DataSet内数据实体（百分比）的文本颜色
            pieData.setValueTextSize(12f);          //设置所有DataSet内数据实体（百分比）的文本字体大小
            //pieData.setValueTypeface(mTfLight);     //设置所有DataSet内数据实体（百分比）的文本字体样式
            pieData.setValueFormatter(PercentFormatter());//设置所有DataSet内数据实体（百分比）的文本字体格式
            pieChart.setData(pieData);

            binding.todayTv.setText("本日花費                                                    "+todaycost.toString())

            val list = ArrayList<ExampleItem>()

            for (i in 0 until todaypricelist.size) {
                val item = ExampleItem(todayitemlist.get(i),todaypricelist.get(i).toInt())
                list+=item
            }
            recyclerView.addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL))
            recyclerView.adapter=ExampleAdapter(list)
            recyclerView.layoutManager = LinearLayoutManager(this.context)
            recyclerView.setHasFixedSize(true)

        }

        // Inflate the layout for this fragment
        return binding.root
    }

}