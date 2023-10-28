package com.example.gestureapp.activities.laps

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import androidx.wear.widget.WearableLinearLayoutManager
import com.example.gestureapp.R
import com.example.gestureapp.gestures.Gestures

class LapsActivity : FragmentActivity(), Gestures.WristGestureListener {

    private val lapTimes = mutableListOf<String>()
    private lateinit var sensorLogic: Gestures
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laps)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        viewPager = findViewById(R.id.lapsViewPager)
        viewPager.adapter = LapsActivityAdapter(this)
        viewPager.offscreenPageLimit = 1

        sensorLogic = Gestures(this)
        sensorLogic.setWristGestureListener(this)

    }

    fun addLapTime(lapTime: String) {
        lapTimes.add(lapTime)
        val adapter = LapsFragmentAdapter(lapTimes)
        val lapsFragment = supportFragmentManager.fragments[1] as LapsFragment
        lapsFragment.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun getLapTimes(): List<String> {
        return lapTimes
    }

    fun clearLapTimes() {
        lapTimes.clear()
        val adapter = LapsFragmentAdapter(lapTimes)
        val lapsFragment = supportFragmentManager.fragments[1] as LapsFragment
        lapsFragment.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
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
        val timeFragment = supportFragmentManager.fragments[0] as TimeFragment
        val lapsFragment = supportFragmentManager.fragments[1] as LapsFragment
        val currentFragment = supportFragmentManager.findFragmentByTag("f${viewPager.currentItem}")
        if (currentFragment is TimeFragment && currentFragment.isVisible) {
            timeFragment.startStopTimer()
        } else {
            val layoutManager = lapsFragment.recyclerView.layoutManager as LinearLayoutManager
            val currentPosition = layoutManager.findLastVisibleItemPosition()
            Log.d("d", currentPosition.toString())
            lapsFragment.recyclerView.scrollToPosition(currentPosition + 1)
        }
    }

    override fun onWristGestureFlickDownDetected() {
        val timeFragment = supportFragmentManager.fragments[0] as TimeFragment
        val lapsFragment = supportFragmentManager.fragments[1] as LapsFragment
        val currentFragment = supportFragmentManager.findFragmentByTag("f${viewPager.currentItem}")
        if (currentFragment is TimeFragment && currentFragment.isVisible) {
            timeFragment.resetTimer()
        } else {
            val layoutManager = lapsFragment.recyclerView.layoutManager as LinearLayoutManager
            val currentPosition = layoutManager.findFirstVisibleItemPosition()
            Log.d("d", currentPosition.toString())
            lapsFragment.recyclerView.scrollToPosition(currentPosition - 1)
        }
    }

    override fun onWristGestureUpDetected() {
        val timeFragment = supportFragmentManager.fragments[0] as TimeFragment
        timeFragment.addLap()
    }

    override fun onWristGestureDownDetected() {
        onBackPressed()
    }

    override fun onWristGestureSideLeftDetected() {
        viewPager.setCurrentItem(0, true)
    }

    override fun onWristGestureSideRightDetected() {
        viewPager.setCurrentItem(1, true)
    }
}