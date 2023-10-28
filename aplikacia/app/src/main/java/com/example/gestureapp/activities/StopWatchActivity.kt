package com.example.gestureapp.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.gestureapp.R
import com.example.gestureapp.databinding.ActivityStopWatchBinding
import com.example.gestureapp.gestures.Gestures

class StopWatchActivity : Activity(), Gestures.WristGestureListener {

    private lateinit var binding: ActivityStopWatchBinding
    private var timerStarted = false
    private var handler: Handler = Handler(Looper.getMainLooper())
    private var time = 0.0
    private var elapsedTime = 0L
    private lateinit var sensorLogic: Gestures

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStopWatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorLogic = Gestures(this)
        sensorLogic.setWristGestureListener(this)

        binding.stopwatchStartPauseButton.setOnClickListener{ startStopTimer() }
        binding.stopwatchResetButton.setOnClickListener{ resetTimer() }

    }

    override fun onStart() {
        super.onStart()
        sensorLogic.startSensors()
    }

    override fun onStop() {
        super.onStop()
        sensorLogic.stopSensors()
    }

    private fun resetTimer() {
        pauseTimer()
        time = 0.0
        elapsedTime = 0L
        binding.stopwatchTime.text = getTimeStringFromDouble(time)
    }

    private fun startStopTimer() {
        if (timerStarted)
            pauseTimer()
        else
            startTimer()
    }

    private fun startTimer() {
        Log.i("tag","START")
        binding.stopwatchStartPauseButton.apply {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.pause)
        }
        timerStarted = true

        elapsedTime = System.currentTimeMillis()

        handler.post(object : Runnable {
            override fun run() {
                val currentTime = System.currentTimeMillis()
                time += (currentTime - elapsedTime) / 1000.0
                elapsedTime = currentTime
                binding.stopwatchTime.text = getTimeStringFromDouble(time)
                handler.postDelayed(this, 10)
            }
        })
    }

    private fun pauseTimer() {
        binding.stopwatchStartPauseButton.apply {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.start)
        }
        timerStarted = false

        handler.removeCallbacksAndMessages(null)
        Log.i("tag","PAUSE")
    }

    private fun getTimeStringFromDouble(time: Double): CharSequence? {
        val minutes = time.toInt() % 3600 / 60
        val seconds = time.toInt() % 60
        val milliseconds = ((time - time.toInt()) * 1000).toInt()

        return makeTimeString(minutes, seconds, milliseconds)
    }

    private fun makeTimeString(minutes: Int, seconds: Int, milliseconds: Int): String {
        val formattedMilliseconds = String.format("%03d", milliseconds)
        return String.format("%02d:%02d:%s", minutes, seconds, formattedMilliseconds.substring(0, 2))
    }

    override fun onWristGestureFlickUpDetected() {
        startStopTimer()
    }

    override fun onWristGestureFlickDownDetected() {
        resetTimer()
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
