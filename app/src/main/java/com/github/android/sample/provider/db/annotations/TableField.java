package com.github.android.sample.provider.db.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author :Created by cz
 * @date 2019-06-21 16:24
 * 设定表不同字段名,防止字段修改,字段值不同
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableField {
    /**
     * 字段名
     *
     * @return
     */
    String value();

    /**
     * 标记主键
     *
     * @return
     */
    boolean primaryKey() default false;

    /**
     * 主键自增长
     *
     * @return
     */
    boolean autoIncrement() default false;
}
