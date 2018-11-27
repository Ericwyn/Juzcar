package com.ericwyn.juzcar.scan.analysis;

import com.ericwyn.juzcar.config.PackageName;
import com.ericwyn.juzcar.scan.cb.ApiAnalysisCb;
import com.ericwyn.juzcar.scan.cb.ParamAnalysisCb;
import com.ericwyn.juzcar.scan.obj.ApiType;
import com.ericwyn.juzcar.scan.obj.JuzcarApi;
import com.ericwyn.juzcar.scan.obj.JuzcarParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
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
            put(PackageName.RequestParam, ParamAnalysis.org_springframework_web_bind_annotation_RequestParam);
        }
    };

    /**
     * 对 org.springframework.web.bind.annotation.RequestMapping 注解的分析回调
     */
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
                // 先获取所在 Controller 的 RequestMapping 里面的 path
                String[] controllerURLs = getControllerURLFromMethod(method);
                if (null != controllerURLs && controllerURLs.length != 0){
                    String[] absolutePath = new String[controllerURLs.length * paths.length];
                    int i=0;
                    for (String controllerUrl : controllerURLs){
                        for (String methodUrl : paths){
                            absolutePath[i++] = (controllerUrl+"/"+methodUrl).replaceAll("//","/");
                        }
                    }
                    api.setUrl(absolutePath);
                }else {
                    api.setUrl(paths);
                }
                // 设置 api 支持的方法
                String[] apiMethods = new String[methods.length];
                for(int i = 0; i < methods.length; i++){
                    apiMethods[i] = methods[i].toString();
                }
                api.setMethod(apiMethods);
                // 设置 api 需要的参数
                getParamFromMethodToApi(method, api);

                return api;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    };

    /**
     * 对 org.springframework.web.bind.annotation.PostMapping 注解的分析回调
     */
    public static ApiAnalysisCb org_springframework_web_bind_annotation_PostMapping = new ApiAnalysisCb() {
        @Override
        public JuzcarApi analysis(Method method, Annotation annotation) {
            try {
                JuzcarApi api = new JuzcarApi();

                // 分析 org.springframework.web.bind.annotation.PostMapping 的方法
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
                // 先获取所在 Controller 的 RequestMapping 里面的 path
                String[] controllerURLs = getControllerURLFromMethod(method);
                if (null != controllerURLs && controllerURLs.length != 0){
                    String[] absolutePath = new String[controllerURLs.length * paths.length];
                    int i=0;
                    for (String controllerUrl : controllerURLs){
                        for (String methodUrl : paths){
                            absolutePath[i++] = (controllerUrl+"/"+methodUrl).replaceAll("//","/");
                        }
                    }
                    api.setUrl(absolutePath);
                }else {
                    api.setUrl(paths);
                }
                // 设置 api 支持的方法
                api.setMethod(new String[]{"POST"});
                // 获取原本 api 中的参数，用以分析 api 需要的参数
                getParamFromMethodToApi(method, api);

                return api;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    };

    /**
     * 对 org.springframework.web.bind.annotation.GetMapping 注解的分析回调
     */
    public static ApiAnalysisCb org_springframework_web_bind_annotation_GetMapping = new ApiAnalysisCb() {
        @Override
        public JuzcarApi analysis(Method method, Annotation annotation) {
            try {
                JuzcarApi api = new JuzcarApi();

                // 分析 org.springframework.web.bind.annotation.GetMapping 的方法
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
                // 先获取所在 Controller 的 RequestMapping 里面的 path
                String[] controllerURLs = getControllerURLFromMethod(method);
                if (null != controllerURLs && controllerURLs.length != 0){
                    String[] absolutePath = new String[controllerURLs.length * paths.length];
                    int i=0;
                    for (String controllerUrl : controllerURLs){
                        for (String methodUrl : paths){
                            absolutePath[i++] = (controllerUrl+"/"+methodUrl).replaceAll("//","/");
                        }
                    }
                    api.setUrl(absolutePath);
                }else {
                    api.setUrl(paths);
                }
                // 设置 api 支持的方法
                api.setMethod(new String[]{"GET"});
                // 设置 api 需要的参数
                getParamFromMethodToApi(method, api);
                return api;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    };

    /**
     * 从 Method 里面获取 Param 具体信息并且塞到 api 里面去
     * @param method
     * @param api
     */
    private static void getParamFromMethodToApi(Method method, JuzcarApi api){
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

    /**
     * 从一个 Controller 里面的 Method
     * 获取原 Controller 可能存在的 RequestMapping 注解
     *
     * @param method
     * @return 返回注解的具体 path
     */
    private static String[] getControllerURLFromMethod(Method method){
        Class<?> controller = method.getDeclaringClass();
        for (Annotation annotation : controller.getAnnotations()){
            if (annotation.annotationType().getName().equals(PackageName.RequestMapping)){
                Method getValue = null;
                try {
                    getValue = annotation.annotationType().getMethod("value");
                    return (String[]) getValue.invoke(annotation);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    return new String[]{};
                }
            }
        }
        return new String[]{};
    }

    private static ApiType getApiType(Method method){
        // 如果该方法有 ResponseBody 注解，那么类型一定是 ApiType.JSON 或者 ApiType.XML
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations){
            if (annotation.annotationType().getName().equals(PackageName.ResponseBody)){
                // TODO 判断到底是 JSON 还是 XML
                return ApiType.JSON;
            }
        }
        // 获取类所在的 Controller 的 Controller 注解类型
        // TODO 这里效率不高，因为调用 getControllerURLFromMethod 又重复的获取了一次 declaringClass
        Class<?> controller = method.getDeclaringClass();
        for (Annotation annotation : controller.getAnnotations()){
            if (annotation.annotationType().getName().equals(PackageName.Controller)){
                return null;
            }
        }
        return null;
    }
}
