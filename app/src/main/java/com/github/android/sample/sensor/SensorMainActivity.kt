package com.github.android.sample.sensor

import android.content.Context
import android.content.Intent
import android.hardware.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_sensor_main.*

/**
 * 陀螺仪测试
 * https://www.kancloud.cn/kancloud/android-tutorial/87281
 * 加速度计传感器和陀螺仪传感器始终基于硬件。
 * 1. Sensor.TYPE_ACCELEROMETER 加速度传感器；
 * 2. Sensor.TYPE_GYROSCOPE 陀螺仪
 * 3. Sensor.TYPE_LIGHT 光线
 * 4. Sensor.TYPE_ORIENTATION 方向
 *
 */
class SensorMainActivity : ToolbarActivity(), SensorEventListener {
    // 1.传感器管理器
    private var sensorManager: SensorManager? = null
    // 2.加速度传感器
    private var mSensorAccelerometer: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // google 的例子
        btn_accelerometer1.setOnClickListener {
            startActivity(Intent(this, AccelerometerPlayActivity::class.java))
        }

        // 加速度
        btn_accelerometer.setOnClickListener {
            accelerometor()
        }
    }


    // 加速度
    private fun accelerometor() {
        mSensorAccelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager?.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager?.unregisterListener(this)
    }


    /////////////// sensor listener
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return
        }

        val a = event.values  // x,y,z
        Log.d("sensor, 加速度", "x:${a[0]},y:${a[1]},z:${a[2]}")
    }
    /////////////// sensor listener


}
