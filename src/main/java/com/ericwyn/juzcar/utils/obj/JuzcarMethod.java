package com.ericwyn.juzcar.utils.obj;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ericwyn on 18-11-26.
 */
public class JuzcarMethod {
    private Method method;
    private Map<String, Annotation> annotationMap;

    public JuzcarMethod(){
    }

    public JuzcarMethod(Method method, Annotation[] annotations) {
        this.method = method;
        annotationMap = new HashMap<>();
        for (Annotation an : annotations){
            annotationMap.put(an.annotationType().getName(), an);
        }
    }

    public Method getMethod() {
        return method;
    }

    public Map<String, Annotation> getAnnotationMap() {
        return annotationMap;
    }

}
