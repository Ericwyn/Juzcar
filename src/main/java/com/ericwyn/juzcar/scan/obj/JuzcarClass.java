package com.ericwyn.juzcar.scan.obj;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * 用来保存 Juzcar 扫描到的 Controller Class 信息
 * 保存一些注解信息，减少后续使用的时候再度的反射获取
 *
 * Created by Ericwyn on 18-11-26.
 */
public class JuzcarClass {
    private Class clazz;
    private Map<String, Annotation> annotationMap;

    public JuzcarClass() {
    }

    public JuzcarClass(Class clazz, Annotation[] annotations) {
        this.clazz = clazz;
        annotationMap = new HashMap<>();
        for (Annotation an : annotations){
            annotationMap.put(an.annotationType().getName(), an);
        }
    }

    public Class getClazz() {
        return clazz;
    }

    public Map<String, Annotation> getAnnotationMap() {
        return annotationMap;
    }
}
