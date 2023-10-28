package com.example.watchsensors

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.example.watchsensors.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var gyroscope: Sensor

    private lateinit var accText: TextView
    private lateinit var gyroText: TextView
    private lateinit var button: Button
    private var printable: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        accText = findViewById(R.id.acc_text)
        gyroText = findViewById(R.id.gyro_text)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(gyroscopeListener, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private val accelerometerListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val data = "ACC\nX: $x\nY: $y\nZ: $z\n"

                accText.text = data

                if (printable) {
                    print(data)
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Do nothing
        }
    }

    private val gyroscopeListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_GYROSCOPE) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val data = "GYR\nX: $x\nY: $y\nZ: $z\n"

                gyroText.text = data
                if (printable) {
                    print(data)
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Do nothing
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(accelerometerListener)
        sensorManager.unregisterListener(gyroscopeListener)
    }
    private fun onButtonClick() {
        GlobalScope.launch {
            println("\n\nBUTTON START\n")
            delay(1000)
            println("BUTTON END\n\n")
        }
    }
}