package com.github.android.sample.jetpack.architecture

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.android.sample.R
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import org.jetbrains.anko.toast
import androidx.core.content.ContextCompat.getSystemService


/**
 * lifecycle
 */
class LifeCycleTest1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_cycle_test1)
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                !== PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                        ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
            requestPermissions(this,
                    arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
                    22)
        } else {
            lifecycle
            bindLocationListener()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>,
                                            @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            bindLocationListener()
        } else {
            Toast.makeText(this, "This sample requires Location access", Toast.LENGTH_LONG).show()
        }
    }

    private fun bindLocationListener() {
        BoundLocationManager(this, this, object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                val textView: TextView = findViewById(R.id.location)
                textView.text = location?.getLatitude().toString() + ", " + location?.getLongitude()
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
                Toast.makeText(this@LifeCycleTest1Activity,
                        "Provider enabled: $provider", Toast.LENGTH_SHORT).show()
            }

            override fun onProviderDisabled(provider: String?) {
            }
        })
    }

    //////////////////////////////////
    /**
     * LifecycleObserver 是一个空接口，结合注解 OnLifecycleEvent 使用
     */
    class BoundLocationManager(val context: Context,
                               val lifeCycleOwner: LifecycleOwner,
                               val locationListener: LocationListener
    ) :
            LifecycleObserver {

        private var mLocationManager: LocationManager? = null

        init {
            // 添加观察者
            lifeCycleOwner.lifecycle.addObserver(this)
        }


        @SuppressLint("MissingPermission")
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun addLocationListener() {
            mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
            mLocationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0F, locationListener)
            Log.d("BoundLocationMgr", "Listener added")

            // Force an update with the last location, if available.
            val lastLocation = mLocationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastLocation != null) {
                locationListener.onLocationChanged(lastLocation)
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun removeLocationListener() {
            if (mLocationManager == null) {
                return;
            }
            mLocationManager?.removeUpdates(locationListener)
            mLocationManager = null;
            Log.d("BoundLocationMgr", "Listener removed")
        }
    }
}
