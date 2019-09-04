package com.github.android.sample.provider.db.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author :Created by cz
 * @date 2019-06-21 16:22
 * 字段过滤,用于过滤字段
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldFilter {
    boolean value() default true;
}
