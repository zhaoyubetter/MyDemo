package com.github.better.app.annotationapi;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.github.better.app.annotationapi.api.ViewBinder;
import com.github.better.app.annotationapi.api.ViewFinder;
import com.github.better.app.annotationapi.impl.ActivityViewFinder;

import java.util.HashMap;
import java.util.Map;

public final class BetterKnife {

    private static final ActivityViewFinder activityViewFinder = new ActivityViewFinder();
    private static final Map<String, ViewBinder> binderMap = new HashMap();

    public static void bind(@NonNull Activity activity) {
        bind(activity, activity, activityViewFinder);
    }

    public static void unBind(Object object) {
        String className = object.getClass().getName();
        final ViewBinder viewBinder = binderMap.get(className);
        if (viewBinder != null) {
            viewBinder.unBindView(object);
            binderMap.remove(className);
        }
    }

    /**
     * '注解绑定
     *
     * @param host   表示注解 View 变量所在的类，也就是注解类
     * @param object 表示查找 View 的地方，Activity & View 自身就可以查找，Fragment 需要在自己的 itemView 中查找
     * @param finder ui绑定提供者接口
     */
    private static void bind(Object host, Object object, ViewFinder finder) {
        String className = host.getClass().getName();       // 获取注解类全名
        try {
            ViewBinder binder = binderMap.get(className);
            if (binder == null) {       // 创建对象
                Class<?> clazz = Class.forName(className + "$$ViewBinder");
                binder = (ViewBinder) clazz.newInstance();
                binderMap.put(className, binder);
            }
            if (binder != null) {
                binder.bindView(host, object, finder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
