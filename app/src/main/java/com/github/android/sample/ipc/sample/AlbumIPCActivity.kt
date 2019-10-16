package com.github.android.sample.ipc.sample

import android.app.Activity
import android.content.ComponentName
import android.content.Intent

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_album_ipc.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import com.github.android.sample.third.GlideEngine
import com.yu.bundles.album.MaeAlbum
import android.widget.Toast
import com.better.base.toast
import com.yu.bundles.album.AlbumListener
import com.yu.bundles.album.ConfigBuilder
import org.json.JSONObject


/**
 * 扩进程调用相册（AlbumIPCActivity 运行在另外进程）
 * 传参数，一定要使用 intent
 */
class AlbumIPCActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_ipc)
        setTitleFromIntent(intent)

        // 相册
        btn_open.setOnClickListener {
            MaeAlbum.setImageEngine(GlideEngine())
            MaeAlbum.from(this@AlbumIPCActivity)
                    .maxSize(8)
                    .column(3)
                    .fileType(ConfigBuilder.FILE_TYPE.IMAGE)
                    .setIsShowCapture(false)
                    .forResult(object : AlbumListener {
                        override fun onSelected(ps: List<String>) {   // 选择完毕回调
                            showResult(ps)
                        }
                        override fun onFull(ps: List<String>, p: String) {  // 选满了的回调
                            Toast.makeText(applicationContext, "选满了", Toast.LENGTH_SHORT).show()
                        }
                    })
        }

        // onActivityResult回调
        btn_openActivity.setOnClickListener {
            startActivityForResult(Intent(this@AlbumIPCActivity, IPCTest1Activity::class.java).apply {
                putExtra("setData", "from other activity")
            }, 12)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val str = data?.getStringExtra("data")
        val json = JSONObject(str)
        toast(json)
    }

    private fun showResult(list: List<String>) {
        txt_result.text = list.reduce { init, next -> init + "\n" + next }
    }
}
