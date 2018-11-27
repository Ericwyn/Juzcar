//package com.ericwyn.juzcar.utils;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//
//import java.text.SimpleDateFormat;
//
///**
// *
// * Json 处理工具，底层调用了 Jackson
// *
// * Created by Ericwyn on 18-11-26.
// */
//public class JsonUtils {
//    private static ObjectMapper mapper;
//    public static ObjectMapper getMapper(){
//        if (null == mapper){
//            synchronized (JsonUtils.class){
//                if (null == mapper){
//                    mapper = new ObjectMapper();
//                    //让Json可以缩进，可读性更好，一般用在测试和开发阶段。
//                    mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
//                    //让map的key按自然顺序排列
//                    mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
//                    //日期输出格式
//                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
//                    mapper.setDateFormat(outputFormat);
//                }
//            }
//        }
//        return mapper;
//    }
//
//}
