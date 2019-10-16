package com.github.android.sample.problem.webview

import android.os.Bundle
import android.webkit.WebView
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import android.graphics.Bitmap
import android.net.Uri
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebViewClient
import android.webkit.WebSettings
import kotlinx.android.synthetic.main.activity_web_view_swipe_conflict.*

/**
 * webview 里头的h5的下拉与原生下拉刷新控件冲突了。
 * 通过获取webview 的 url 参数，来禁用原生的下拉刷新
 */
class WebViewSwipeConflictActivity : ToolbarActivity() {

    var isRefresh = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view_swipe_conflict)
        setWebView(webview)
        webview.loadUrl("http://m.jd.com")
        webview.reload()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.webview_problem_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_forbidden -> {       // 禁用下拉
                isRefresh = !isRefresh
                webview.loadUrl(if (isRefresh) "http://m.jd.com" else "http://m.jd.com?refresh=no")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setWebView(webview: WebView) {

        val webSettings = webview.settings
//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true)
// 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
// 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

//设置自适应屏幕，两者合用
        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true) // 缩放至屏幕的大小

//缩放操作
        webSettings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true) //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false) //隐藏原生的缩放控件

//其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK) //关闭webview中缓存
        webSettings.setAllowFileAccess(true) //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true) //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true) //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8")//设置编码格式

        webview.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                swipe.isRefreshing = true
            }

            // 链接跳转都会走这个方法
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)   // 强制在当前 WebView 中加载 url
                Uri.parse(url).apply {
                    swipe.isEnabled = ("no" != this.getQueryParameter("refresh"))
                }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                swipe.isRefreshing = false
            }
        }
    }
}
