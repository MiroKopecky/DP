package com.example.gestureapp.activities.laps

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class LapsActivityAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TimeFragment()
            1 -> LapsFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}