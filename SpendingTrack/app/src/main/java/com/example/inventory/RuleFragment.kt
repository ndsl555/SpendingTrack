package com.example.inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.inventory.databinding.FragmentRuleBinding
import java.util.*


class RuleFragment : Fragment() {
    var calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"))
    val thisyear=calendar.get(Calendar.YEAR).toString()
    val thismonth=(calendar.get(Calendar.MONTH)+1).toString()
    val thisday=calendar.get(Calendar.DATE).toString()
    private var _binding:FragmentRuleBinding?=null
    private val binding get() = _binding!!
    private val viewModel: RuleViewModel by activityViewModels {
        RuleViewModelFactory(
            (activity?.application as InventoryApplication).database.RuleDao()
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRuleBinding.inflate(inflater, container, false)

        binding.buttonlimit.setOnClickListener {
            val num = binding.editTextlimit.text.toString()
            if (num.isNullOrBlank()){
                Toast.makeText(this.requireContext(),"尚未輸入本月預算", Toast.LENGTH_SHORT).show()
            }
            else{
                viewModel.addNewItem(num.toInt())
                Toast.makeText(this.requireContext(),thismonth+"月份預算:"+num+"元", Toast.LENGTH_SHORT).show()
                println(num)
            }
            //sharedViewModel.setbudget(num)

        }
        //println(viewModel.allruleItems.value?.size)


        return binding.root
    }


}