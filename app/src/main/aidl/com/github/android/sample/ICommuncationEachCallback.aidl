// ICommuncationEachCallback.aidl
package com.github.android.sample;

// Declare any non-default types here with import statements

interface ICommuncationEachCallback {
    //异步接口执行完毕后 使用该方法回调返回结果
    void onComplete(inout Bundle bundle);
}