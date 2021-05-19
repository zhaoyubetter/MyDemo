// ICommuncationEachInterface.aidl
package com.github.android.sample;

import com.github.android.sample.ICommuncationEachCallback;

// Declare any non-default types here with import statements

interface ICommuncationEachInterface {
     // 死亡监听
     void setDeathRecipient(IBinder iBinder, String str);
     // ICommuncationEachCallback 为异步接口回调对象 当异步方法执行完毕时通过该接口回调结果
     // 需要在子线程发起 IPC 调用，bundle 可以直接写返回值，callback 可以不使用。
     void sendAsync(inout Bundle bundle, ICommuncationEachCallback callback);
     Bundle sendSync(inout Bundle bundle);
}