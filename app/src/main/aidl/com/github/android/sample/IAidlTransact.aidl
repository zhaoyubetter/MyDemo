// IAidlTransact.aidl
package com.github.android.sample;

// Declare any non-default types here with import statements

interface IAidlTransact {
    // in 表示数据只能由客户端流向服务端，
        // out 表示数据只能由服务端流向客户端，
        // inout 则表示数据可在服务端与客户端之间双向流通
       void transactSync(inout Bundle bundle);
       void transactAsync(inout Bundle bundle);
}
