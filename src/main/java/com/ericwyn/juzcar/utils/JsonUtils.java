package com.ericwyn.juzcar.utils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * Json 处理工具，底层调用了 Jackson
 *
 * Created by Ericwyn on 18-11-26.
 */
public class JsonUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    /**
     * 对 Object 尝试类型检测，将其合适地转换为 JSON 字符串返回。
     *
     * @param obj 任意对象
     * @return JSON 字符串
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Boolean || obj instanceof Number) {
            return obj.toString();
        } else if (obj instanceof String) {
            return '\"' + obj.toString().replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") + '\"';
        } else if (obj instanceof String[]) {
            return jsonArr((String[]) obj, v -> "\"" + v + "\"");
        } else if (obj.getClass() == Integer[].class) {
            return jsonArr((Integer[]) obj, v -> v + "");
        } else if (obj.getClass() == int[].class) {
            Integer[] arr = Arrays.stream((int[]) obj).boxed().toArray(Integer[]::new);
            return jsonArr(arr, v -> v + "");
        } else if (obj instanceof Long[]) {
            return jsonArr((Long[]) obj, v -> v.toString());
        } else if (obj instanceof long[]) {
            Long[] arr = Arrays.stream((long[]) obj).boxed().toArray(Long[]::new);
            return jsonArr(arr, v -> v.toString());
        } else if (obj instanceof Date) {
            return '\"' + sdf.format((Date) obj) + '\"';
        } else if (obj instanceof Map) {
            return stringifyMap((Map<?, ?>) obj);
        }
        // TODO 更多数据类型的JSON解析
//        else if (obj instanceof Map[]) {
//            return jsonArr((Map<?, ?>[]) obj, JsonHelper::stringifyMap);
//        }

        else if (obj instanceof List) {
            List<?> list = (List<?>) obj;

            if (list.size() > 0) {
                if (list.get(0) instanceof Integer) {
                    return toJson(list.toArray(new Integer[list.size()]));
                } else if (list.get(0) instanceof String) {
                    return toJson(list.toArray(new String[list.size()]));
                } else if (list.get(0) instanceof Map) { // Map 类型的输出
                    return toJson(list.toArray(new Map[list.size()]));
                }
            } else {
                return "[]";
            }
        } else if (obj instanceof Object[]) {
            return jsonArr((Object[]) obj, JsonUtils::toJson);
        } else if (obj instanceof Object) { // 普通 Java Object
            List<String> arr = new ArrayList<>();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                String key = field.getName();
                if (key.indexOf("this$") != -1)
                    continue;

                Object _obj = null;
                try {
                    _obj = field.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                arr.add('\"' + key + "\":" + toJson(_obj));
            }

            return '{' + String.join(",", arr) + '}';
        }

        return null;
    }

    /**
     * 输入任意类型数组，在 fn 作适当的转换，返回 JSON 字符串
     *
     * @param o 数组
     * @param fn 元素处理器，返回元素 JSON 字符串
     * @return 数组的 JSON 字符串
     */
    public static <T> String jsonArr(T[] o, Function<T, String> fn) {
        if (o.length == 0)
            return "[]";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < o.length; i++) {
            sb.append(fn.apply((T) o[i]));
            if (i != (o.length - 1))
                sb.append(", ");
        }

        return '[' + sb.toString() + ']';
    }

    /**
     *
     * @param list
     * @param fn 元素处理器，返回元素 JSON 字符串
     * @return
     */
    static <T> String eachList(List<T> list, Function<T, String> fn) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            sb.append(fn.apply(list.get(i)));
            if (i != (list.size() - 1))
                sb.append(", ");
        }

        return '[' + sb.toString() + ']';
    }

    /**
     * 输入一个 Map，将其转换为 JSON Str
     *
     * @param map 输入数据
     * @return JSON 字符串
     */
    public static String stringifyMap(Map<?, ?> map) {
        if (map == null)
            return null;

        if (map.size() == 0)
            return "{}";

        List<String> arr = new ArrayList<>();
        for (Object key : map.keySet())
            arr.add('\"' + key.toString() + "\":" + toJson(map.get(key)));

        return '{' + String.join(",", arr) + '}';
    }

    /**
     * Bean 转换为 JSON 字符串
     *
     * 传入任意一个 Java Bean 对象生成一个指定规格的字符串
     *
     * @param bean bean对象
     * @return String "{}"
     */
    public static String beanToJson(Object bean) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        PropertyDescriptor[] props = null;

        try {
            props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        if (props != null) {
            for (int i = 0; i < props.length; i++) {
                try {
                    String name = "\"" + props[i].getName() + "\"";
                    String value = toJson(props[i].getReadMethod().invoke(bean));

                    json.append(name);
                    json.append(":");
                    json.append(value);
                    json.append(",");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }

        return json.toString();
    }

    /**
     * 删除 JSON 注释
     *
     * @param str 待处理的字符串
     * @return 删除注释后的字符串
     */
    public static String removeComemnt(String str) {
        return str.replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*+\\/", "");
    }

    /**
     * 输出到 JSON 文本时候的换行
     *
     * @param str JSON 字符串
     * @return
     */
    public static String jsonString_covernt(String str) {
        return str.replace("\r\n", "\\n");
    }

    /**
     *
     * @param str JSON 字符串
     * @return
     */
    public static String javaValue2jsonValue(String str) {
        return str.replaceAll("\"", "\\\\\"").replaceAll("\t", "\\\\\t");
    }
}
