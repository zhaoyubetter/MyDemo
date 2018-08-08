// IForecaseAidlInterface.aidl
package com.github.android.sample;

import com.github.android.sample.ForecaseEntity;
import com.github.android.sample.IForecaseAidlListener;

// Declare any non-default types here with import statements

interface IForecaseAidlInterface {

    // 避免耗时卡死主线程，采用接口回调的方式
    // ForecaseEntity getForecase(in String cityCode);

    void getForecase(in String cityCode, in IForecaseAidlListener listener);
}
