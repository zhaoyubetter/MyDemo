package com.github.android.sample.thridlib.tencent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.android.sample.R
import com.tencent.tencentmap.mapsdk.maps.MapView
import com.tencent.tencentmap.mapsdk.maps.TencentMap
import kotlinx.android.synthetic.main.fragment_map_base.*


/**
 * @author zhaoyu1  2020-04-01
 **/
class MapUseFragment : Fragment() {

    private var mapView: MapView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mark()
        // 添加地图
        btn_add.setOnClickListener {
            initMap()
            settingMap()
            settingUI()
        }
        // 移除
        btn_del.setOnClickListener {
            mapView?.let {
                (view as ViewGroup).removeView(it)
                it.onDestroy()
                mapView = null
            }
        }
        // 交通
        chk_traffic.setOnCheckedChangeListener { _, isChecked ->
            mapView?.map?.isTrafficEnabled = isChecked
        }
        // 卫星
        chk_satellite.setOnCheckedChangeListener { _, isChecked ->
            mapView?.map?.mapType = if (isChecked) TencentMap.MAP_TYPE_SATELLITE else TencentMap.MAP_TYPE_NORMAL
        }
    }

    private fun initMap() {
        if (mapView == null) {
            mapView = MapView(this.context!!)
            mapView?.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            (view as ViewGroup).addView(mapView)
        }
        mapView?.onResume()
    }

    /**
     *  地图设置 - TencentMap，不然：卫星地图、交通等
     */
    private fun settingMap() {
        //获取TencentMap实例
        val tencentMap = mapView?.getMap()
//        isTrafficEnabled
        tencentMap?.setCameraCenterProportion(39.0f, 116.0f)
        //设置缩放级别
    }

    /**
     * UiSettings类用于设置地图的视图状态，如Logo位置设置、比例尺位置设置、地图手势开关等
     */
    private fun settingUI() {
        val uiSystem = mapView?.map?.uiSettings
        uiSystem?.isRotateGesturesEnabled = true    // 不允许旋转
        uiSystem?.isMyLocationButtonEnabled = true  // 定位按钮
        uiSystem?.isCompassEnabled = true       // 指南针
    }


    // 标注 Marker 操作
    private fun mark() {
        btn_add_marker.setOnClickListener {

        }
        btn_rm_marker.setOnClickListener {

        }
    }


    override fun onDestroy() {
        mapView?.onDestroy()
        super.onDestroy()
    }

    override fun onPause() {
        mapView?.onPause()
        super.onPause()
    }

    override fun onResume() {
        mapView?.onResume()
        super.onResume()
    }

    override fun onStop() {
        mapView?.onStop()
        super.onStop()
    }
}