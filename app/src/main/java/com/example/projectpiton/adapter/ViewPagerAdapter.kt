package com.example.projectpiton.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projectpiton.view.DailyFragment
import com.example.projectpiton.view.MonthlyFragment
import com.example.projectpiton.view.WeeklyFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                DailyFragment()
            }
            1->{
                WeeklyFragment()
            }
            2->{
                MonthlyFragment()
            }
            else->{
                Fragment()
            }
        }
    }
}