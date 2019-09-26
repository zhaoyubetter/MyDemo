package com.github.android.sample.provider;

import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.test.runner.AndroidJUnitRunner;

/**
 * @author zhaoyu1  2019-09-26
 **/
public class TestRunner extends AndroidJUnitRunner
{
    @Override
    public void onCreate(Bundle arguments)
    {
        MultiDex.install(getTargetContext());
        super.onCreate(arguments);
    }
}
