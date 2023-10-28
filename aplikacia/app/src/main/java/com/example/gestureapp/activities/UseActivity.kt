package com.example.gestureapp.activities

import android.app.Activity
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.wear.widget.WearableLinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import com.example.gestureapp.CustomScrollingLayoutCallback
import com.example.gestureapp.R
import com.example.gestureapp.databinding.ActivityUseBinding
import com.example.gestureapp.gestures.Gestures

class UseActivity : Activity(), Gestures.WristGestureListener {

    private lateinit var binding: ActivityUseBinding
    private lateinit var recyclerView: WearableRecyclerView
    private lateinit var useCarousel0: ImageView
    private lateinit var useCarousel1: ImageView
    private lateinit var useCarousel2: ImageView
    private lateinit var useCarousel3: ImageView
    private lateinit var useCarousel4: ImageView
    private lateinit var useCarousel5: ImageView
    private lateinit var useCarousel6: ImageView
    private lateinit var sensorLogic: Gestures

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        sensorLogic = Gestures(this)
        sensorLogic.setWristGestureListener(this)

        recyclerView = findViewById(R.id.use_recycler_view)
        recyclerView.requestFocus()

        useCarousel0 = findViewById(R.id.use_carousel_0)
        useCarousel1 = findViewById(R.id.use_carousel_1)
        useCarousel2 = findViewById(R.id.use_carousel_2)
        useCarousel3 = findViewById(R.id.use_carousel_3)
        useCarousel4 = findViewById(R.id.use_carousel_4)
        useCarousel5 = findViewById(R.id.use_carousel_5)
        useCarousel6 = findViewById(R.id.use_carousel_6)

        recyclerView.apply {
            isEdgeItemsCenteringEnabled = true
            layoutManager = WearableLinearLayoutManager(this@UseActivity)
        }
        recyclerView.layoutManager =
            WearableLinearLayoutManager(this, CustomScrollingLayoutCallback())

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        val adapter = UseAdapter()
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                useCarousel0.backgroundTintList = if (visibleItemPosition == 0) {
                    ColorStateList.valueOf(ContextCompat.getColor(this@UseActivity, R.color.green_picked))
                } else {
                    null
                }
                useCarousel1.backgroundTintList = if (visibleItemPosition == 1) {
                    ColorStateList.valueOf(ContextCompat.getColor(this@UseActivity, R.color.green_picked))
                } else {
                    null
                }
                useCarousel2.backgroundTintList = if (visibleItemPosition == 2) {
                    ColorStateList.valueOf(ContextCompat.getColor(this@UseActivity, R.color.green_picked))
                } else {
                    null
                }
                useCarousel3.backgroundTintList = if (visibleItemPosition == 3) {
                    ColorStateList.valueOf(ContextCompat.getColor(this@UseActivity, R.color.green_picked))
                } else {
                    null
                }
                useCarousel4.backgroundTintList = if (visibleItemPosition == 4) {
                    ColorStateList.valueOf(ContextCompat.getColor(this@UseActivity, R.color.green_picked))
                } else {
                    null
                }
                useCarousel5.backgroundTintList = if (visibleItemPosition == 5) {
                    ColorStateList.valueOf(ContextCompat.getColor(this@UseActivity, R.color.green_picked))
                } else {
                    null
                }
                useCarousel6.backgroundTintList = if (visibleItemPosition == 6) {
                    ColorStateList.valueOf(ContextCompat.getColor(this@UseActivity, R.color.green_picked))
                } else {
                    null
                }
            }
        })

    }

    override fun onStart() {
        super.onStart()
        sensorLogic.startSensors()
    }

    override fun onStop() {
        super.onStop()
        sensorLogic.stopSensors()
    }

    override fun onWristGestureFlickUpDetected() {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val currentPosition = layoutManager.findLastVisibleItemPosition()
        recyclerView.scrollToPosition(currentPosition + 1)
    }

    override fun onWristGestureFlickDownDetected() {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val currentPosition = layoutManager.findFirstVisibleItemPosition()
        recyclerView.scrollToPosition(currentPosition - 1)
    }

    override fun onWristGestureUpDetected() {
        null //do nothing
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
