package com.example.gestureapp.gestures

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.*
import kotlin.math.abs

class Gestures(private val context: Context) : SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var gyroscope: Sensor

    private val minFlickArray = mutableListOf<Float>()
    private val maxFlickArray = mutableListOf<Float>()
    private val minUpDownArray = mutableListOf<Float>()
    private val maxUpDownArray = mutableListOf<Float>()
    private val minSideArray = mutableListOf<Float>()
    private val maxSideArray = mutableListOf<Float>()

    private var wristGestureListener: WristGestureListener? = null
    private var isHandHorizontal: Boolean = false

    private companion object {
        const val HAND_HORIZONTAL_MIN = -4
        const val HAND_HORIZONTAL_MAX = 4
        const val FLICK_MIN = -4
        const val FLICK_MAX = 4
        const val UP_DOWN_MIN = -3
        const val UP_DOWN_MAX = 4
        const val SIDE_MIN = -3
        const val SIDE_MAX = 3
    }

    interface WristGestureListener {
        fun onWristGestureUpDetected()
        fun onWristGestureDownDetected()
        fun onWristGestureFlickUpDetected()
        fun onWristGestureFlickDownDetected()
        fun onWristGestureSideLeftDetected()
        fun onWristGestureSideRightDetected()
    }

    fun setWristGestureListener(listener: WristGestureListener) {
        wristGestureListener = listener
    }

    fun startSensors() {
        sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST)
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_FASTEST)
    }

    fun stopSensors() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            isHandHorizontal = (x > HAND_HORIZONTAL_MIN) && (x < HAND_HORIZONTAL_MAX)
        }
        else if (event?.sensor?.type == Sensor.TYPE_GYROSCOPE) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // ACC horizontal position check
            if (isHandHorizontal) {
                // GYR FLICK UP & FLICK DOWN gesture
                if (x < FLICK_MIN) minFlickArray.add(x)
                if (x > FLICK_MAX) maxFlickArray.add(x)
                if ((x >= FLICK_MIN) && (x <= FLICK_MAX) && (maxFlickArray.size >= 3) && (minFlickArray.size >= 3)) {
                    val minValue = minFlickArray.minOrNull()
                    val maxValue = maxFlickArray.maxOrNull()
                    if ((abs(minValue!!)) > maxValue!!) {
                        clearAllGestures()
                        onWristGestureFlickUpDetected()
                    } else if ((abs(minValue)) <= maxValue) {
                        clearAllGestures()
                        onWristGestureFlickDownDetected()
                    }
                }
            }
            // GYR UP & DOWN gesture
            if (y < UP_DOWN_MIN) minUpDownArray.add(y)
            if (y > UP_DOWN_MAX) maxUpDownArray.add(y)
            if ((y >= UP_DOWN_MIN) && (y <= UP_DOWN_MAX) && (maxUpDownArray.size >= 3) && (minUpDownArray.size >= 3)) {
                val minValue = minUpDownArray.minOrNull()
                val maxValue = maxUpDownArray.maxOrNull()
                if ((abs(minValue!!)) > maxValue!!) {
                    clearAllGestures()
                    onWristGestureUpDetected()
                } else if ((abs(minValue)) <= maxValue) {
                    clearAllGestures()
                    onWristGestureDownDetected()
                }
            }
            //GYR SIDE gesture
            if (z < SIDE_MIN) minSideArray.add(z)
            if (z > SIDE_MAX) maxSideArray.add(z)
            if ((z >= SIDE_MIN) && (z <= SIDE_MAX) && (minSideArray.size >= 3) && (maxSideArray.size >= 3) && (minUpDownArray.size < minSideArray.size)) {
                val minValue = minSideArray.minOrNull()
                val maxValue = maxSideArray.maxOrNull()
                if ((abs(minValue!!)) > maxValue!!) {
                    clearAllGestures()
                    onWristGestureSideRightDetected()
                } else if ((abs(minValue)) <= maxValue) {
                    clearAllGestures()
                    onWristGestureSideLeftDetected()
                }
            }
        }

    }

    private fun onWristGestureFlickUpDetected() {
        wristGestureListener?.onWristGestureFlickUpDetected()
    }

    private fun onWristGestureFlickDownDetected() {
        wristGestureListener?.onWristGestureFlickDownDetected()
    }

    private fun onWristGestureDownDetected() {
        wristGestureListener?.onWristGestureDownDetected()
    }

    private fun onWristGestureUpDetected() {
        wristGestureListener?.onWristGestureUpDetected()
    }

    private fun onWristGestureSideLeftDetected() {
        wristGestureListener?.onWristGestureSideLeftDetected()
    }

    private fun onWristGestureSideRightDetected() {
        wristGestureListener?.onWristGestureSideRightDetected()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing
    }

    private fun clearAllGestures() {
        minFlickArray.clear()
        maxFlickArray.clear()
        minUpDownArray.clear()
        maxUpDownArray.clear()
        minSideArray.clear()
        maxSideArray.clear()
    }

}