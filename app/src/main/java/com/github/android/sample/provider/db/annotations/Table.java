package com.github.android.sample.provider.db.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author :Created by cz
 * @date 2019-06-21 16:22
 * 表注解,设定名称,设定主键等
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    /**
     * 表名
     * @return
     */
    String value() default "";
    /**
     * 标记主键
     * @return
     */
    String primaryKey() default "";

    /**
     * 主键自增长
     * @return
     */
    boolean autoIncrement() default false;
    /**
     * 是否暴露
     * @return
     */
    boolean exported() default false;
}
