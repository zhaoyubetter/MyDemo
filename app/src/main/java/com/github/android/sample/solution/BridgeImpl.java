package com.github.android.sample.solution;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.webkit.WebView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 【js协议】：
 * me://jsbridge?data={"type":"1","attach":{"msg":"你好，better"}}&callback=304594818
 * // * ////////////////////////////
 * <p>
 * 【供js调用的原生方法】；
 * 规则：
 * 1. 每个方法都是public static final 方法，且方法名不能重复，方法都是3个固定参数 (webview, json格式的param, callback js 回调)
 * 2. 每个方法的 json 格式可能不同，需要明确说明，jsonParam 对应 协议中的：attach; callback对应协议中的callback值，表示回调地址
 * 3. 如需回调，每个方法，必须表面json格式的详细说明；
 * <p>
 * 【回调参数格式说明】：
 * 如：{"code":0, "msg":"ok", "data": {"key":"value", "key1":"value1"}}
 * 1. code: 0 表示成功，1 失败；
 * 2. msg: 表示消息
 * 3. data: 具体json数据
 */
public class BridgeImpl implements IBrige {

    /**
     * 获取typeMethod映射表
     *
     * @return
     */
    public synchronized static final Map<String, String> getType2Method() {
        Map<String, String> map = new HashMap<>();
        map.put("toast", "showToast");
        map.put("dialog", "showDialog");
        map.put("album", "openAlbum");
        return map;
    }


    /**
     * 原生toast
     *
     * @param webView
     * @param param    参数格式：  {"msg":""}
     * @param callback 回传格式：  {"code":0, "msg":"ok", "data": {"key":"value", "key1":"value1"}}
     */
    public final static void showToast(WebView webView, JSONObject param, final Callback callback) {
        String message = param.optString("msg");
        Toast.makeText(webView.getContext(), message, Toast.LENGTH_SHORT).show();
        if (null != callback) {
            try {
                JSONObject object = new JSONObject();
                object.put("key", "value");
                object.put("key1", "value1");
                callback.apply(getJSONObject(0, "ok", object));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 原生dialog
     *
     * @param webView
     * @param param   参数格式：  {"msg":""}
     */
    public final static void showDialog(final WebView webView, final JSONObject param, final Callback callback) {
        String message = param.optString("msg");
        new AlertDialog.Builder(webView.getContext()).setTitle("better").setMessage(message)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    /**
     * 打开原生相册
     *
     * @param webView
     * @param param
     * @param callback
     */
    public final static void openAlbum(final WebView webView, final JSONObject param, final Callback callback) {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        webView.getContext().startActivity(intent);
    }

    private final static JSONObject getJSONObject(int code, String msg, JSONObject result) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("msg", msg);
            object.putOpt("data", result);
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 调用H5页面的制定方法
     * 参数：
     * param1：参数1

    public static void callH5Func(final Activity webLoader, WebView wv, JSONObject param, final Callback callback) {
        final String handleName = param.optString("handleName");
        wv.post(new Runnable() {
            public void run() {
                //做一些自己的操作，操作完毕后将值通过回调回传给h5页面
                try {
                    JSONObject object = new JSONObject();
                    object.put("param1", "传给h5的参数~");
                    //主动调用h5中注册的方法
                    callback.apply(getJSONObject(0, "ok", object));
                    callback.call(webLoader, handleName, JSBridge.getSuccessJSONObject(object));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
     */

}
