package com.github.android.sample.jetpack.architecture.frag

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.github.android.sample.R
import kotlinx.android.synthetic.main.fragment_lifecycle_frag2.*

class LifecycleFrag2 : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_lifecycle_frag2, container, false)
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>,
                                            @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            bindLocationListener()
        } else {
            Toast.makeText(context!!, "This sample requires Location access", Toast.LENGTH_LONG).show()
        }
    }

    private fun bindLocationListener() {
        BoundLocationManager(activity!!, this, object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                locationTxt.text = location?.getLatitude().toString() + ", " + location?.getLongitude()
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
                Toast.makeText(activity!!,
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
