package com.example.gestureapp.activities.laps

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.gestureapp.R
import com.example.gestureapp.databinding.FragmentTimeBinding

class TimeFragment : Fragment() {

    private lateinit var binding: FragmentTimeBinding
    private var timerStarted = false
    private var handler: Handler = Handler(Looper.getMainLooper())
    private var time = 0.0
    private var elapsedTime = 0L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewPager = activity?.findViewById<ViewPager2>(R.id.lapsViewPager)

        binding = FragmentTimeBinding.inflate(inflater, container, false)

        binding.lapsLaps.setOnClickListener{
            viewPager?.setCurrentItem(1, true)
        }

        binding.lapsLap.setOnClickListener{ addLap() }
        binding.lapsStartPauseButton.setOnClickListener{ startStopTimer() }
        binding.lapsResetButton.setOnClickListener{ resetTimer() }

        return binding.root
    }

    fun addLap() {
        val lapTime = getTimeStringFromDouble(time).toString()
        (activity as LapsActivity).addLapTime(lapTime)
    }

    fun resetTimer() {
        pauseTimer()
        time = 0.0
        elapsedTime = 0L
        binding.lapsTime.text = getTimeStringFromDouble(time)
        (activity as LapsActivity).clearLapTimes()
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

    private fun pauseTimer() {
        binding.lapsStartPauseButton.apply {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.start)
        }
        timerStarted = false
        handler.removeCallbacksAndMessages(null)
    }
    fun startStopTimer() {
        if (timerStarted)
            pauseTimer()
        else
            startTimer()
    }

    private fun startTimer() {
        binding.lapsStartPauseButton.apply {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.pause)
        }
        timerStarted = true

        elapsedTime = System.currentTimeMillis()

        handler.post(object : Runnable {
            override fun run() {
                val currentTime = System.currentTimeMillis()
                time += (currentTime - elapsedTime) / 1000.0
                elapsedTime = currentTime
                binding.lapsTime.text = getTimeStringFromDouble(time)
                handler.postDelayed(this, 10)
            }
        })
    }

}
