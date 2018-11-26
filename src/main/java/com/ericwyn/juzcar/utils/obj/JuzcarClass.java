package com.ericwyn.juzcar.utils.obj;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * 用来保存 Juzcar 扫描到的 Controller Class 信息
 * 保存一些注解信息，减少后续使用的时候再度的反射获取
 *
 * Created by Ericwyn on 18-11-26.
 */
public class JuzcarClass {
    private Class clazz;
    private List<Annotation> annotations;
    private List<String> annotationNames;

    public JuzcarClass() {
    }

    public JuzcarClass(Class clazz, List<Annotation> annotations) {
        this.clazz = clazz;
        setAnnotations(annotations);
    }

    public Class getClazz() {
        return clazz;
    }

    private void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    private void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
        this.annotationNames = new ArrayList<>();
        for (Annotation an : annotations){
            this.annotationNames.add(an.annotationType().getName());
        }
    }

    public List<String> getAnnotationNames() {
        return annotationNames;
    }
}
