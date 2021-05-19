// ICommuncationEachClientInterface.aidl
package com.github.android.sample;

// Declare any non-default types here with import statements

//服务端（宿主进程）主动给客户端发送消息接口监听
interface ICommuncationEachClientInterface {
     // 这里就使用同步吧
//      /**
//      * requestBundle 必须包含：
//      * name: 事件名称
//      * data: 事件参数，json 格式字符串
//      * appIds: 发送给哪里, 字符串数组
//      */
      Bundle sendEventToMp(in Bundle requestBundle);
}