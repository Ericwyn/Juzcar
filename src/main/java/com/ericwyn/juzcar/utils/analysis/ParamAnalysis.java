package com.ericwyn.juzcar.utils.analysis;

import com.ericwyn.juzcar.utils.cb.ParamAnalysisCb;
import com.ericwyn.juzcar.utils.obj.JuzcarParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 存储对各种被标记 Parameter 的 Analysis
 *
 * Created by Ericwyn on 18-11-26.
 */
public class ParamAnalysis {

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
                    // TODO 其他类型的参数
                }
                return juzcarParam;
            }catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException nme){
                nme.printStackTrace();
                return null;
            }
        }
    };
}
