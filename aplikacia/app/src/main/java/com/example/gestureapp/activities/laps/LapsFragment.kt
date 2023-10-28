package com.example.gestureapp.activities.laps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.viewpager2.widget.ViewPager2
import androidx.wear.widget.WearableLinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import com.example.gestureapp.CustomScrollingLayoutCallback
import com.example.gestureapp.R
import com.example.gestureapp.databinding.FragmentLapsBinding


class LapsFragment : Fragment() {

    lateinit var recyclerView: WearableRecyclerView
    private lateinit var binding: FragmentLapsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewPager = activity?.findViewById<ViewPager2>(R.id.lapsViewPager)

        binding = FragmentLapsBinding.inflate(inflater, container, false)

        binding.lapsTimes.setOnClickListener{
            viewPager?.setCurrentItem(0, true)
        }

        recyclerView = binding.lapsRecyclerView
        recyclerView.requestFocus()

        val list = (activity as LapsActivity).getLapTimes()
        val adapter = LapsFragmentAdapter(list)
        recyclerView.adapter = adapter


        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        return binding.root
    }

}