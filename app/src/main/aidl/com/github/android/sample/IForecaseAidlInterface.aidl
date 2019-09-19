// IForecaseAidlInterface.aidl
package com.github.android.sample;

import com.github.android.sample.ForecaseEntity;
import com.github.android.sample.IForecaseAidlListener;
import android.os.Bundle;
import com.github.android.sample.Book;

// Declare any non-default types here with import statements

interface IForecaseAidlInterface {

    // 避免耗时卡死主线程，采用接口回调的方式
    // ForecaseEntity getForecase(in String cityCode);

    void getForecase(in String cityCode, in IForecaseAidlListener listener);

    // in 表示数据只能由客户端流向服务端，
    // out 表示数据只能由服务端流向客户端，
    // inout 则表示数据可在服务端与客户端之间双向流通
    void transactSync(inout Bundle bundle);

    // 直接传递对象，不使用 Bundle 组织
    void transactObj(in Book book);
}
