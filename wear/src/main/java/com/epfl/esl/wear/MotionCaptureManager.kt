package com.epfl.esl.simongame.wear

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.concurrent.CopyOnWriteArrayList

class MotionCaptureManager(context: Context) {

    private val sm = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val gyroSensor = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    suspend fun captureWindow(durationMs: Long): Pair<List<Vec3Sample>, List<Vec3Sample>> =
        withContext(Dispatchers.Default) {

            val accel = CopyOnWriteArrayList<Vec3Sample>()
            val gyro = CopyOnWriteArrayList<Vec3Sample>()

            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    val t = SystemClock.elapsedRealtime()
                    val s = Vec3Sample(
                        tMs = t,
                        x = event.values[0],
                        y = event.values[1],
                        z = event.values[2]
                    )
                    when (event.sensor.type) {
                        Sensor.TYPE_ACCELEROMETER -> accel.add(s)
                        Sensor.TYPE_GYROSCOPE -> gyro.add(s)
                    }
                }
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            accelSensor?.let { sm.registerListener(listener, it, SensorManager.SENSOR_DELAY_GAME) }
            gyroSensor?.let { sm.registerListener(listener, it, SensorManager.SENSOR_DELAY_GAME) }

            delay(durationMs)

            sm.unregisterListener(listener)

            accel.toList() to gyro.toList()
        }
}
