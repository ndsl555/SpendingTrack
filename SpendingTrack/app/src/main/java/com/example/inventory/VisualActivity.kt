package com.example.inventory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.inventory.databinding.ActivityMainBinding
import com.example.inventory.databinding.ActivityVisualBinding
import com.google.android.material.tabs.TabLayoutMediator

class VisualActivity : AppCompatActivity() {

    private val tabTitleArray= arrayOf("今日消費狀況","本月消費狀況")
    private lateinit var binding: ActivityVisualBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityVisualBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.apply {
            binding.root.findViewById<ViewPager2>(R.id.viewpager2).adapter=ViewPagerAdapter(supportFragmentManager,lifecycle)
            TabLayoutMediator(binding.root.findViewById(R.id.tabLayout),binding.root.findViewById(R.id.viewpager2)){
                tab,position->
                tab.text=tabTitleArray[position]
            }.attach()
        }
    }
}