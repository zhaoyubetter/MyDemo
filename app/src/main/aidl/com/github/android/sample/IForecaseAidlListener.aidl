// IForecaseAidlListener.aidl
package com.github.android.sample;

// Declare any non-default types here with import statements
import com.github.android.sample.ForecaseEntity;

interface IForecaseAidlListener {
    void onResult(in ForecaseEntity entity);
}
