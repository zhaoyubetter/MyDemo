package com.github.better.app.annotationapi.api;

/**
 * UI绑定解绑接口
 */
public interface ViewBinder<T> {
    /**
     * 注解绑定
     *
     * @param host   表示注解 View 变量所在的类，也就是注解类
     * @param object 表示查找 View 的地方，Activity & View 自身就可以查找，Fragment 需要在自己的 itemView 中查找
     * @param finder ui绑定提供者接口
     */
    void bindView(T host, Object object, ViewFinder finder);

    /**
     * @param host
     */
    void unBindView(T host);
}
