package com.github.better.app.annotationapi.impl;

import android.app.Activity;
import android.view.View;

import com.github.better.app.annotationapi.api.ViewFinder;

public class ActivityViewFinder implements ViewFinder {
    @Override
    public View findView(Object object, int resId) {
        return ((Activity) object).findViewById(resId);
    }
}
