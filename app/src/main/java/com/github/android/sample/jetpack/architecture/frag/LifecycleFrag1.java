package com.github.android.sample.jetpack.architecture.frag;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.github.android.sample.R;

public class LifecycleFrag1 extends Fragment implements View.OnClickListener {

    private final String TAG = "lifeCycle";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lifecycle_1_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_resume).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        MyLifeObserver observer = new MyLifeObserver();
        if (v.getId() == R.id.btn_resume) {
            getLifecycle().addObserver(observer);
        }
    }

    /**
     * jdk7 注解形式，已不推荐使用
     */
    class MyLifeObserver implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        public void myStart() {
            Log.d(TAG, "移除 observer -> ON_START");
            getLifecycle().removeObserver(this);
            getLifecycle().addObserver(new DefaultLifecycleObserver() {
                @Override
                public void onStart(@NonNull LifecycleOwner owner) {
                    Log.d(TAG, "child -> onStart");
                }

                // 会走
                @Override
                public void onResume(@NonNull LifecycleOwner owner) {
                    Log.d(TAG, "child -> onResume");
                }
            });
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void myResume() {
            Log.d(TAG, "ON_RESUME");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLifecycle().addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onCreate(@NonNull LifecycleOwner owner) {
                Log.d(TAG, "onCreate");
            }

            @Override
            public void onStart(@NonNull LifecycleOwner owner) {
                Log.d(TAG, "onStart");
            }

            @Override
            public void onResume(@NonNull LifecycleOwner owner) {
                Log.d(TAG, "onResume");
            }

            @Override
            public void onPause(@NonNull LifecycleOwner owner) {
                Log.d(TAG, "onPause");
            }

            @Override
            public void onStop(@NonNull LifecycleOwner owner) {
                Log.d(TAG, "onStop");
            }

            @Override
            public void onDestroy(@NonNull LifecycleOwner owner) {
                Log.d(TAG, "onDestroy");
            }
        });
    }

}