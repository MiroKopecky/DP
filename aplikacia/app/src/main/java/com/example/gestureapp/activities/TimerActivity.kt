package com.example.gestureapp.activities

import android.app.Activity
import android.content.Context
import android.os.*
import android.widget.Button
import android.widget.TextView
import com.example.gestureapp.R
import com.example.gestureapp.databinding.ActivityTimerBinding
import com.example.gestureapp.gestures.Gestures

class TimerActivity : Activity(), Gestures.WristGestureListener {

    private lateinit var binding: ActivityTimerBinding
    private lateinit var timerTextView: TextView
    private lateinit var addButton: Button
    private lateinit var reduceButton: Button
    private lateinit var startResetButton: Button
    private var timerRunning = false
    private var timerValue = 0L
    private lateinit var timer: CountDownTimer
    private var vibrator: Vibrator? = null
    private lateinit var sensorLogic: Gestures

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorLogic = Gestures(this)
        sensorLogic.setWristGestureListener(this)

        timerTextView = findViewById(R.id.timer_time)
        addButton = findViewById(R.id.timer_plus_button)
        reduceButton = findViewById(R.id.timer_minus_button)
        startResetButton = findViewById(R.id.timer_start_reset_button)

        addButton.setOnClickListener { addMinute() }
        reduceButton.setOnClickListener { reduceMinute() }
        startResetButton.setOnClickListener { startResetTimer() }

        startResetButton.isEnabled = false

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    }

    override fun onStart() {
        super.onStart()
        sensorLogic.startSensors()
    }

    override fun onStop() {
        super.onStop()
        vibrator?.cancel()
        if (timerRunning) {
            timer.cancel()
        }
        sensorLogic.stopSensors()
    }

    private fun addMinute() {
        timerValue += 60_000
        updateTimerTextView()
        startResetButton.isEnabled = true
    }

    private fun reduceMinute() {
        timerValue -= 60_000
        if (timerValue < 0) {
            timerValue = 0
        }
        updateTimerTextView()
    }

    private fun startResetTimer() {
        if (timerRunning) {
            resetTimer()
        } else {
            startTimer()
        }

        addButton.isEnabled = !timerRunning
        reduceButton.isEnabled = !timerRunning
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timerValue, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerValue = millisUntilFinished
                updateTimerTextView()
            }

            override fun onFinish() {
                timerRunning = false
                timerValue = 0
                updateTimerTextView()
                startResetButton.apply {
                    setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.start, 0, 0)
                }
                addButton.isEnabled = true
                reduceButton.isEnabled = true

                vibrator?.let { v ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && timerValue != -1L) {
                        v.vibrate(
                            VibrationEffect.createOneShot(500,
                                VibrationEffect.DEFAULT_AMPLITUDE))
                    }
                    else {
                        v.vibrate(500)
                    }
                }
            }
        }
        timer.start()
        timerRunning = true
        startResetButton.apply {
            setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.reset, 0, 0)
        }
    }

    private fun resetTimer() {
        timer.cancel()
        timerValue = 0
        updateTimerTextView()
        timerRunning = false
        startResetButton.apply {
            setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.start, 0, 0)
        }
    }

    private fun updateTimerTextView() {
        val minutes = timerValue / 60_000
        val seconds = (timerValue % 60_000) / 1000
        val minutesString = String.format("%02d", minutes)
        val secondsString = String.format("%02d", seconds)
        timerTextView.text = "$minutesString:$secondsString"
        startResetButton.isEnabled = timerValue != 0L
    }

    override fun onWristGestureFlickUpDetected() {
        if (addButton.isEnabled) {
            addMinute()
        }
    }

    override fun onWristGestureFlickDownDetected() {
        if (reduceButton.isEnabled) {
            reduceMinute()
        }
    }

    override fun onWristGestureUpDetected() {
        if (startResetButton.isEnabled) {
            startResetButton.performClick()
        }
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