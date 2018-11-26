package com.ericwyn.juzcar.utils.analysis;

import com.ericwyn.juzcar.utils.cb.ApiAnalysisCb;
import com.ericwyn.juzcar.utils.cb.ParamAnalysisCb;
import com.ericwyn.juzcar.utils.obj.JuzcarApi;
import com.ericwyn.juzcar.utils.obj.JuzcarParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;

/**
 * 存储各种对于被标记的 Method 的 Analysis
 * 命名规则就是对应的需要分析的注解的完整类名，将 . 替换成下划线
 *
 * Created by Ericwyn on 18-11-26.
 */
public class ApiAnalysis {

    public static HashMap<String, ParamAnalysisCb> parameAnalysisMap = new HashMap<String, ParamAnalysisCb>(){
        {
            // 分析被 RequestParam 标记的方法参数
            put("org.springframework.web.bind.annotation.RequestParam", ParamAnalysis.org_springframework_web_bind_annotation_RequestParam);
        }
    };


    public static ApiAnalysisCb org_springframework_web_bind_annotation_RequestMapping = new ApiAnalysisCb() {
        @Override
        public JuzcarApi analysis(Method method, Annotation annotation) {
            try {
                JuzcarApi api = new JuzcarApi();

                // 分析 org.springframework.web.bind.annotation.RequestMapping 的方法
                Method Params = annotation.annotationType().getMethod("params");
                Method headers = annotation.annotationType().getMethod("headers");
                Method consumes = annotation.annotationType().getMethod("consumes");
                Method produces = annotation.annotationType().getMethod("produces");
                Method getName = annotation.annotationType().getMethod("name");
                Method getValue = annotation.annotationType().getMethod("value");
                Method getMethod = annotation.annotationType().getMethod("method");

                // 获取需要的数据
                String[] paths = (String[]) getValue.invoke(annotation);
                Object[] methods = (Object[]) getMethod.invoke(annotation);
                String name = (String) getName.invoke(annotation);

                // 设置 api 的 name
                api.setName(name);
                // 设置 api 的 url
                api.setUrl(paths);
                // 设置 api 支持的方法
                String[] apiMethods = new String[methods.length];
                for(int i = 0; i < methods.length; i++){
                    apiMethods[i] = methods[i].toString();
                }
                api.setMethod(apiMethods);
                // 设置 api 需要的参数
                getParameFromMethodToApi(method, api);

                return api;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    };

    public static ApiAnalysisCb org_springframework_web_bind_annotation_PostMapping = new ApiAnalysisCb() {
        @Override
        public JuzcarApi analysis(Method method, Annotation annotation) {
            try {
                JuzcarApi api = new JuzcarApi();

                // 分析 org.springframework.web.bind.annotation.RequestMapping 的方法
                Method Params = annotation.annotationType().getMethod("params");
                Method headers = annotation.annotationType().getMethod("headers");
                Method consumes = annotation.annotationType().getMethod("consumes");
                Method produces = annotation.annotationType().getMethod("produces");
                Method getName = annotation.annotationType().getMethod("name");
                Method getValue = annotation.annotationType().getMethod("value");

                // 获取需要的数据
                String[] paths = (String[]) getValue.invoke(annotation);
                String name = (String) getName.invoke(annotation);

                // 设置 api 的 name
                api.setName(name);
                // 设置 api 的 url
                api.setUrl(paths);
                // 设置 api 支持的方法
                api.setMethod(new String[]{"POST"});
                // 获取原本 api 中的参数，用以分析 api 需要的参数
                getParameFromMethodToApi(method, api);

                return api;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    };

    public static ApiAnalysisCb org_springframework_web_bind_annotation_GetMapping = new ApiAnalysisCb() {
        @Override
        public JuzcarApi analysis(Method method, Annotation annotation) {
            try {
                JuzcarApi api = new JuzcarApi();

                // 分析 org.springframework.web.bind.annotation.RequestMapping 的方法
                Method Params = annotation.annotationType().getMethod("params");
                Method headers = annotation.annotationType().getMethod("headers");
                Method consumes = annotation.annotationType().getMethod("consumes");
                Method produces = annotation.annotationType().getMethod("produces");
                Method getName = annotation.annotationType().getMethod("name");
                Method getValue = annotation.annotationType().getMethod("value");

                // 获取需要的数据
                String[] paths = (String[]) getValue.invoke(annotation);
                String name = (String) getName.invoke(annotation);

                // 设置 api 的 name
                api.setName(name);
                // 设置 api 的 url
                api.setUrl(paths);
                // 设置 api 支持的方法
                api.setMethod(new String[]{"GET"});
                // 设置 api 需要的参数
                getParameFromMethodToApi(method, api);
                return api;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    };

    private static void getParameFromMethodToApi(Method method, JuzcarApi api){
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters){
            Annotation[] annotations = parameter.getAnnotations();
            ParamAnalysisCb parameAnalysis;
            for (Annotation an : annotations){
                String analysisKey = an.annotationType().getName();
                parameAnalysis = parameAnalysisMap.get(analysisKey);
                // 参数的注解
                if (parameAnalysis != null){
                    JuzcarParam param = parameAnalysis.analysis(parameter, an);
                    api.getParams().add(param);
                }
            }
        }
    }

}
