package com.example.gestureapp

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.wear.widget.WearableLinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import com.example.gestureapp.databinding.ActivityMainBinding
import com.example.gestureapp.gestures.Gestures

class MainActivity : Activity(), Gestures.WristGestureListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: WearableRecyclerView
    private lateinit var sensorLogic: Gestures

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorLogic = Gestures(this)
        sensorLogic.setWristGestureListener(this)

        recyclerView = findViewById(R.id.main_recycler_view)
        recyclerView.requestFocus()

        val adapter = MainAdapter(sensorLogic)
        recyclerView.adapter = adapter

        recyclerView.apply {
            isEdgeItemsCenteringEnabled = true
            layoutManager = WearableLinearLayoutManager(this@MainActivity)
        }
        recyclerView.layoutManager = WearableLinearLayoutManager(this, CustomScrollingLayoutCallback())

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
    }

    override fun onStart() {
        super.onStart()
        sensorLogic.startSensors()
    }

    override fun onResume() {
        super.onResume()
        sensorLogic.startSensors()
    }

    override fun onStop() {
        super.onStop()
        sensorLogic.stopSensors()
    }

    override fun onWristGestureFlickUpDetected() {
        val layoutManager = recyclerView.layoutManager as WearableLinearLayoutManager
        val currentPosition = layoutManager.findLastVisibleItemPosition()
        recyclerView.scrollToPosition(currentPosition + 1)
    }

    override fun onWristGestureFlickDownDetected() {
        val layoutManager = recyclerView.layoutManager as WearableLinearLayoutManager
        val currentPosition = layoutManager.findFirstVisibleItemPosition()
        recyclerView.scrollToPosition(currentPosition - 1)
    }

    override fun onWristGestureUpDetected() {
        val layoutManager = recyclerView.layoutManager as? WearableLinearLayoutManager
        val position = layoutManager?.findFirstVisibleItemPosition()
        val holder = recyclerView.findViewHolderForAdapterPosition(position!!) as? MainAdapter.MyViewHolder
        holder?.let {
            it.textView.performClick()
        }
        sensorLogic.stopSensors()
    }

    override fun onWristGestureDownDetected() {
        onBackPressed()
    }

    override fun onWristGestureSideLeftDetected() {
        null //do nothing
    }

    override fun onWristGestureSideRightDetected() {
        null //do nothing
    }

}
