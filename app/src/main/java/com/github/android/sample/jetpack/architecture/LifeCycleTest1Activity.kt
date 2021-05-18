package com.github.android.sample.jetpack.architecture

import android.os.Bundle
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import com.github.android.sample.jetpack.architecture.frag.LifecycleFrag1
import kotlinx.android.synthetic.main.activity_life_cycle_test1.*


/**
 * lifecycle
 */
class LifeCycleTest1Activity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_cycle_test1)
        btn_base.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.container, LifecycleFrag1()).commit()
        }
//        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
//                !== PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this,
//                        ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(this,
//                    arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
//                    22)
//        } else {
//            bindLocationListener()
//        }
    }
}
