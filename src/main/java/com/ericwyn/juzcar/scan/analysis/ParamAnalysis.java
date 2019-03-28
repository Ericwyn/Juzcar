package com.ericwyn.juzcar.scan.analysis;

import com.ericwyn.juzcar.scan.annotations.JuzcarDefaultValue;
import com.ericwyn.juzcar.scan.annotations.JuzcarParamNote;
import com.ericwyn.juzcar.scan.cb.ParamAnalysisCb;
import com.ericwyn.juzcar.scan.obj.JuzcarParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * 存储对各种被标记 Parameter 的 Analysis
 *
 * Created by Ericwyn on 18-11-26.
 */
public class ParamAnalysis {

    /**
     * 解析 @RequestParam 注解的方法参数，变成 JuzcarParam
     */
    public static ParamAnalysisCb org_springframework_web_bind_annotation_RequestParam = new ParamAnalysisCb() {
        @Override
        public JuzcarParam analysis(Parameter parameter, Annotation annotation) {
            try {
                Method getRequired = annotation.annotationType().getMethod("required");
                Method getName = annotation.annotationType().getMethod("name");
                Method getValue = annotation.annotationType().getMethod("value");
                Method getDefaultValue = annotation.annotationType().getMethod("defaultValue");
                Boolean required = (Boolean) getRequired.invoke(annotation);
                String name = getName.invoke(annotation).toString();
                String value = getValue.invoke(annotation).toString();
                // 注解当中 name 和 value 是同义的
                if (name.equals("") && !value.equals("")){
                    name = value;
                }
                if (value.equals("") && !name.equals("")){
                    value = name;
                }
                String defaultValue = getDefaultValue.invoke(annotation).toString();
                JuzcarParam juzcarParam = new JuzcarParam(name, value, defaultValue, required);
                switch (parameter.getType().getName()){
                    case "java.lang.Integer":
                        juzcarParam.setType(Integer.class);
                        break;
                    case "java.lang.String":
                        juzcarParam.setType(String.class);
                        break;
                    case "java.lang.Double":
                        juzcarParam.setType(Double.class);
                        break;
                    // TODOO 其他类型的参数
                    default:
                        juzcarParam.setType(Class.forName(parameter.getType().getName()));
                        break;
                }
                return juzcarParam;
            }catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException nme){
                nme.printStackTrace();
                return null;
            }
        }

        @Override
        public List<JuzcarParam> analysisBody(Parameter parameter, Annotation annotation) {
            return null;
        }
    };

    public static ParamAnalysisCb org_springframework_web_bind_annotation_RequestBody = new ParamAnalysisCb() {
        @Override
        public JuzcarParam analysis(Parameter parameter, Annotation annotation) {
            return null;
        }

        @Override
        public List<JuzcarParam> analysisBody(Parameter parameter, Annotation annotation) {
            List<JuzcarParam> res = new ArrayList<>();
            // 对 @RequestBody 进行分析，反射看看注解的对象，内部有什么属性
            Field[] fields = parameter.getType().getDeclaredFields();
            for (Field field:fields){
                String varName = field.getName();
                if (Modifier.isStatic(field.getModifiers())){
                    // 跳过静态变量
                    continue;
                }
                JuzcarParam juzcarParam = new JuzcarParam();
                juzcarParam.setValue(varName);  // 变量参数名称
                juzcarParam.setType(field.getType());
                juzcarParam.setDefaultValue(null);
                juzcarParam.setName(varName);
                juzcarParam.setRequired(false);  // RequestBody 应该都算是非必须变量吧
                // field 的注解
                Annotation[] annotations = field.getAnnotations();
                for (Annotation an : annotations){
                    if (an instanceof JuzcarParamNote){
                        juzcarParam.setNote(((JuzcarParamNote) an).value());
                    }
                    if (an instanceof JuzcarDefaultValue){
                        juzcarParam.setDefaultValue(((JuzcarDefaultValue) an).value());
                    }
                }
                if (juzcarParam.getNote() == null){
                    juzcarParam.setNote("");
                }
                if (juzcarParam.getDefaultValue() == null){
                    juzcarParam.setDefaultValue("");
                } else {
                    // Magic ，特殊过滤，如果 default 没有设置的时候，那么默认会是这个值
                    if (juzcarParam.getDefaultValue().equals("\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n")){
                        juzcarParam.setDefaultValue("");
                    }
                }
                res.add(juzcarParam);
            }
            return res;
        }
    };

}
