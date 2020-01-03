package com.github.android.sample.problem.webview

import android.os.Bundle
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.*
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
//        webview.loadUrl("https://www.baidu.com")
        webview.loadUrl("file:///android_asset/h5/jsBrige.html") //
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.webview_problem_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_forbidden -> {       // 禁用下拉
                isRefresh = !isRefresh
                webview.loadUrl(if (isRefresh) "https://www.baidu.com" else "https://www.baidu.com?refresh=no")
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
            // 当前webview要加载下一个h5页面时调用，可以截取并通过return值确定是否要本地处理或者交给webview处理，他不拦截资源加载，如：js、css；
            // 参考：https://juejin.im/post/5a5d8ef2f265da3e393a6b76
            //      https://www.jianshu.com/p/3474cb8096da
            // ==》 此方法并非阻止WebView loadUrl时调用系统浏览器
            // ==》若想让WebView loadUrl时，不会调用系统浏览器，需要设置自定的WebViewClient
            // 重写该方法是为了：主要是给WebView提供时机，让其选择是否对UrlLoading进行拦截。 True（拦截WebView加载Url），False（允许WebView加载Url）

            /**
             * WebView的前进、后退、刷新、以及post请求都不会调用shouldOverrideUrlLoading方法，
             * 除去以上行为，还得满足（ ! isLoadUrl || isRedirect） 即 （不是通过webView.loadUrl来加载的 或者 是重定向） 这个条件，
             * 才会调用shouldOverrideUrlLoading方法。
             * 经测试，以下请求会走此方法：
             * <a href="#" onclick="javascript:window.location.reload() ;">刷新</a> <br>
             * <a href="https://www.baidu.com">baidu</a> <br>
             * <a href="#" onclick="javascript:window.location.href='https://www.google.com'">google</a> <br>
             * <a href="test1.html">test1</a> <br>
             */
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                Log.d("better", "url:$url")

//                view.loadUrl(url)   // 强制在当前 WebView 中加载 url
//
//                Uri.parse(url).apply {
//                    swipe.isEnabled = ("no" != this.getQueryParameter("refresh"))
//                }
//                return true
                /**
                该方法返回 true ，则说明由应用的代码处理该 url，WebView 不处理，也就是程序员自己做处理。
                方法返回 false，则说明由 WebView 处理该 url，即用 WebView 加载该 url。
                 */
                return false
            }

            // h5页面请求如.js等资源文件的时调用，可以截取并更换资源文件（用native资源替换h5页面的资源）
            //一个h5页面可能会有多个资源文件请求。
            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                Log.d("better", "request: ${request?.url}")
                return super.shouldInterceptRequest(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                swipe.isRefreshing = false
            }
        }
    }
}
