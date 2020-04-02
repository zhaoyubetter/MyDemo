package com.github.android.sample.thridlib.tencent

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.android.sample.R
import com.github.lib.monitorfragment.MAEPermissionCallback
import com.github.lib.monitorfragment.maeRequestPermission
import com.tencent.map.geolocation.TencentLocation
import com.tencent.map.geolocation.TencentLocationListener
import com.tencent.map.geolocation.TencentLocationManager
import com.tencent.map.geolocation.TencentLocationRequest
import kotlinx.android.synthetic.main.fragment_location_user1.*
import org.jetbrains.anko.support.v4.toast

/**
 * 参考：https://lbs.qq.com/geo/guide-use.html
 *
 * 位置监听器的回调方法分为两类：一类是位置更新时的回调，一类是GPS和Wi-Fi的状态变化回调。
 * @author zhaoyu1  2020-03-23
 **/
class LocationUseFragment : Fragment(), TencentLocationListener {

    val TAG = "loc"

    var locationMgr: LocationManager? = null
    var wifiMgr: WifiManager? = null
    var tencentLocationMgr: TencentLocationManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_location_user1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTencent()
        showInfo()

        // 定位
        btn_start.setOnClickListener {
            startLocation()
        }

        // stop
        btn_stop.setOnClickListener {
            stopLocation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopLocation()
    }

    private fun stopLocation() {
        tencentLocationMgr?.removeUpdates(this) // 移除监听
        locationMgr = null
    }

    private fun startLocation() {

        maeRequestPermission(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), object : MAEPermissionCallback {
            override fun onPermissionApplyFailure(notGrantedPermissions: List<String>, shouldShowRequestPermissions: List<Boolean>) {
            }

            override fun onPermissionApplySuccess() {
                // 创建定位请求
                val request = TencentLocationRequest.create()
                // 修改定位请求参数, 周期为 5000 ms
                request.interval = 5000
                // 设置requestLevel
                // request.setRequestLevel()
                // 开始定位 重复调用 requestLocationUpdates, 将忽略之前的 reqest 并自动取消之前的 listener,
                // 并使用最新的 request 和 listener 继续定位
                val status = tencentLocationMgr?.requestLocationUpdates(request, this@LocationUseFragment)
                if (status == 0) {
                } else {
                    toast("注册监听失败: $status")
                }
            }
        })
    }

    /**
     * 状态回调
     */
    override fun onStatusUpdate(name: String, status: Int, desc: String) {
        val message = ("{name=" + name + ", new status=" + status + ", desc="
                + desc + "}")
        if (status == TencentLocationListener.STATUS_DENIED) {
            /* 检测到定位权限被内置或第三方的权限管理或安全软件禁用, 导致当前应用**很可能无法定位**
			 * 必要时可对这种情况进行特殊处理, 比如弹出提示或引导
			 */
            Toast.makeText(context, "定位权限被禁用!", Toast.LENGTH_SHORT).show()
        }
        Log.d(TAG, message)
        tv_status_info.text = "状态信息: $message"
    }

    /**
     * 位置回调
     */
    override fun onLocationChanged(location: TencentLocation, error: Int, reason: String) {
        var msg: String? = null
        if (error == TencentLocation.ERROR_OK) {
            // 定位成功
            val sb = StringBuilder()
            sb.append("(纬度=").append(location.getLatitude()).append(",经度=")
                    .append(location.getLongitude()).append(",精度=")
                    .append(location.getAccuracy()).append("), 来源=")
                    .append(location.getProvider()).append(", 地址=")
                    .append(location.getAddress())
            msg = sb.toString()
        } else {
            // 定位失败
            msg = "定位失败: $reason"
        }
        tv_info.text = "定位信息: $msg"
        Log.d(TAG, msg)
    }

    private fun initTencent() {
        // 设置火星坐标
        tencentLocationMgr = TencentLocationManager.getInstance(context)
        // 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
        tencentLocationMgr?.coordinateType = TencentLocationManager.COORDINATE_TYPE_GCJ02
    }

    @SuppressLint("SetTextI18n")
    private fun showInfo() {
        locationMgr = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        wifiMgr = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager

        // gps 开关
        val gpsEnabled = try {
            locationMgr?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
        } catch (e: Exception) {
            false
        }

        gps.setText("GPS: ${if (gpsEnabled) "开启" else "关闭"}")
    }
}