package com.ericwyn.juzcar.utils;

import com.ericwyn.juzcar.utils.cb.ApiAnalysis;
import com.ericwyn.juzcar.utils.obj.JuzcarApi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 存储各种 Analysis，类似于静态变量
 * 命名规则就是对应的需要分析的注解的完整类名，将 . 替换成下划线
 *
 * Created by Ericwyn on 18-11-26.
 */
public class JuzcarAnalysis {
    public static ApiAnalysis org_springframework_web_bind_annotation_RequestMapping = new ApiAnalysis() {
        @Override
        public JuzcarApi analysis(Annotation annotation) {
            try {
                JuzcarApi api = new JuzcarApi();

                // 分析 org.springframework.web.bind.annotation.RequestMapping 的方法
                Method Params = annotation.annotationType().getMethod("params");
                Method headers = annotation.annotationType().getMethod("headers");
                Method consumes = annotation.annotationType().getMethod("consumes");
                Method produces = annotation.annotationType().getMethod("produces");
                Method name = annotation.annotationType().getMethod("name");
                Method value = annotation.annotationType().getMethod("value");
                Method method = annotation.annotationType().getMethod("method");

                // 获取需要的数据
                String[] paths = (String[]) value.invoke(annotation);
                Object[] methods = (Object[]) method.invoke(annotation);

                api.setUrl(paths);
                String[] apiMethods = new String[methods.length];
                for(int i = 0; i < methods.length; i++){
                    apiMethods[i] = methods[i].toString();
                }
                api.setMethod(apiMethods);
                return api;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    };

}
