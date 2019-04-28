package com.github.better.app.annotationcompiler;

import com.github.better.annotation.BindView;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * reference: https://www.cnblogs.com/whoislcj/p/6168641.html
 * elements详解
 * https://www.jianshu.com/p/899063e8452e
 * https://blog.csdn.net/wzgiceman/article/details/54580745
 */
public class ViewBinderProcessor extends AbstractProcessor {

    private Filer filer;           // 文件相关的辅助类
    private Elements elementUtils; // 元素相关的辅助类
    private Messager messager;     // 日志相关的辅助类

    private Map<String, AnnotatedClass> annotatedClassMap;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
        messager = processingEnvironment.getMessager();
        annotatedClassMap = new TreeMap<>();
    }

    /**
     * 相当于每个处理器的主函数main()
     * 在这里写扫描、评估和处理注解的代码
     *
     * @param set
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        annotatedClassMap.clear();
        // 扫描整个工程   找出含有BindView注解的元素
        final Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(BindView.class);
        if (elementsAnnotatedWith == null || elementsAnnotatedWith.isEmpty()) {
            return true;
        }

        try {
            processBindView(elementsAnnotatedWith);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            error(e.getMessage());
        }
        for (AnnotatedClass annotatedClass : annotatedClassMap.values()) {
            try {
                annotatedClass.generateFile().writeTo(filer);
            } catch (IOException e) {
                error("Generate file failed, reason: %s", e.getMessage());
            }
        }
        return true;
    }

    /**
     * 这里必须指定，这个注解处理器是注册给哪个注解的。注意，它的返回值是一个字符串的集合，包含本处理器想要处理的注解类型的合法全称
     *
     * @return 注解器所支持的注解类型集合，如果没有这样的类型，则返回一个空集合
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(BindView.class.getCanonicalName());
        return types;
    }

    /**
     * 指定使用的Java版本，通常这里返回SourceVersion.latestSupported()
     *
     * @return 使用的Java版本
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 处理 bindView
     * element是代表程序的一个元素，这个元素可以是：包、类/接口、属性变量、方法/方法形参、泛型参数
     * @param elementsAnnotatedWith
     */
    private void processBindView(Set<? extends Element> elementsAnnotatedWith) {
        for (Element element : elementsAnnotatedWith) {
            // 1. AnnotatedClass
            final TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            final String fullName = typeElement.getQualifiedName().toString();
            error("---> " + fullName);
            AnnotatedClass annotatedClass = annotatedClassMap.get(fullName);
            if (annotatedClass == null) {
                annotatedClass = new AnnotatedClass(typeElement, elementUtils);
                annotatedClassMap.put(fullName, annotatedClass);
            }

            // 2.BindViewField
            final BindViewField bindViewField = new BindViewField(element);
            annotatedClass.addField(bindViewField);
        }
    }

    private void error(String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }
}
