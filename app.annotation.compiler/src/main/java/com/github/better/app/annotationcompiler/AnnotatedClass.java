package com.github.better.app.annotationcompiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * 被注解标注的class
 */
class AnnotatedClass {

    private static class TypeUtil {
        static final ClassName BINDER = ClassName.get("com.github.better.app.annotationapi.api", "ViewBinder");
        static final ClassName PROVIDER = ClassName.get("com.github.better.app.annotationapi.api", "ViewFinder");
    }

    private TypeElement mTypeElement;
    private ArrayList<BindViewField> mFields;
    private Elements mElements;

    AnnotatedClass(TypeElement typeElement, Elements elements) {
        this.mTypeElement = typeElement;
        this.mElements = elements;
        this.mFields = new ArrayList<>();
    }

    void addField(BindViewField field) {
        mFields.add(field);
    }

    JavaFile generateFile() {
        // === generate method: bindView
        MethodSpec.Builder method_bindView_builder = MethodSpec.methodBuilder("bindView")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mTypeElement.asType()), "host", Modifier.FINAL)
                .addParameter(TypeName.OBJECT, "source")
                .addParameter(TypeUtil.PROVIDER, "finder");

        /* $L (原样输出),$S("字符串"),$T(类型) $N(使用代码中定义的方法) */
        for (BindViewField field : mFields) {     // findView, host.field = (field.type) finder.findView(R.id.XXX);
            method_bindView_builder.addStatement("host.$N = ($T)(finder.findView(source, $L));",
                    field.getFieldName(), ClassName.get(field.getFieldType()), field.getResId());
        }

        // === generate method: unBindView
        MethodSpec.Builder method_unBindView_builder = MethodSpec.methodBuilder("unBindView")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mTypeElement.asType()), "host", Modifier.FINAL);

        for (BindViewField field : mFields) {        // host.field = null
            method_unBindView_builder.addStatement("host.$N = null;", field.getFieldName());
        }

        // generate class
        TypeSpec injectClass = TypeSpec.classBuilder(mTypeElement.getSimpleName() + "$$ViewBinder")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.BINDER, TypeName.get(mTypeElement.asType())))     // 泛型接口
                .addMethod(method_bindView_builder.build())
                .addMethod(method_unBindView_builder.build()).build();

        String packageName = mElements.getPackageOf(mTypeElement).getQualifiedName().toString();
        //String packageName = mTypeElement.getQualifiedName();

        return JavaFile.builder(packageName, injectClass).build();
    }
}
