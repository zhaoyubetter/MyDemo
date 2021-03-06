package com.github.better.app.annotationcompiler;

import com.github.better.annotation.BindView;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * BindViewField对象用于被注解的成员变量
 */
class BindViewField {

    private VariableElement variableElement;
    private int resId;

    BindViewField(Element element) {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(String.format("Only fields can be annotated with @%s",
                    BindView.class.getSimpleName()));
        }

        variableElement = (VariableElement) element;

        // 获取注解
        BindView bindView = variableElement.getAnnotation(BindView.class);
        resId = bindView.value();
        if (resId < 0) {
            throw new IllegalArgumentException(
                    String.format("value() in %s for field %s is not valid !", BindView.class.getSimpleName(),
                            variableElement.getSimpleName()));
        }
    }

    /**
     * 获取变量名称
     *
     * @return
     */
    Name getFieldName() {
        return variableElement.getSimpleName();
    }

    /**
     * 获取变量id
     *
     * @return
     */
    int getResId() {
        return resId;
    }

    /**
     * 获取变量类型
     *
     * @return
     */
    TypeMirror getFieldType() {
        return variableElement.asType();
    }
}
