package com.ericwyn.juzcar.utils;

import com.ericwyn.juzcar.IWhat;
import com.ericwyn.juzcar.Juzcar;
import com.ericwyn.juzcar.annotations.JuzcarIgnoreScanner;
import com.ericwyn.juzcar.utils.obj.JuzcarClass;
import com.ericwyn.juzcar.utils.obj.JuzcarMethod;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ericwyn on 18-11-25.
 */
public class ScannerUtils {

    // 应该再写一层分析工具，分析工具是一个 Map<String, Fun> ，然后下面的这些 List，就是这些 Map 的 ketSet
    private static List<String> classAnnotaions = Arrays.asList(
            "org.springframework.stereotype.Controller",
            "org.springframework.web.bind.annotation.RestController"
    );

    private static List<String> methodAnnotaions = Arrays.asList(
            "org.springframework.web.bind.annotation.RequestMapping"
    );

    public static void scanPackage(String iPackage, IWhat what){
        String path = iPackage.replace(".","/");
        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        try {
            // 这里确认 url 是一个文件的位置
            if (url.toString().startsWith("file")){
                String filePath = URLDecoder.decode(url.getFile(),"utf-8");
                File dir = new File(filePath);
                List<File> fileList = new ArrayList<>();
                fetchFileList(dir,fileList);
                for (File f:fileList){
                    String fileName = f.getAbsolutePath();
                    if(fileName.endsWith("class")){
                        // 文件名一般为 /media/ericwyn/Work/Chaos/IntiliJ_Java_Project/juzcart/target/classes/com/ericwyn/juzcart/JuzcartApplication.class
                        // 找到 "classes" 的位置，这个 classes 后面第8个位置开始的后面的文件路径，就是包名字
                        String nosuffixFileName = fileName.substring(8+fileName.lastIndexOf("classes"),fileName.indexOf(".class"));
                        String filePackage = nosuffixFileName.replaceAll("/", ".");
                        Class<?> clazz = Class.forName(filePackage);
                        what.execute(f,clazz);
                    }else {
                        what.execute(f,null);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取工程当中所有的 Controller ，包括 RestController
     * @param initClass
     * @return
     */
    public static List<JuzcarClass> scannerAllController(Class initClass){
        ArrayList<JuzcarClass> res = new ArrayList<>();
        scanPackage(initClass.getPackage().getName(), new IWhat() {
            @Override
            public void execute(File file, Class<?> clazz) {
                Annotation[] annotations = clazz.getAnnotations();
                for (Annotation an : annotations){
                    // 如果 Class 中包含了与 Controller 有关的 注解
                    if (classAnnotaions.contains(an.annotationType().getName().toString())){
                        res.add(new JuzcarClass(clazz, annotations));
                        break;
                    }
                }
            }
        });
        return res;
    }

    /**
     * 清除被 com.ericwyn.juzcar.annotations.JuzcarIgnoreScanner 注解的 Class
     * @param classList
     * @return
     */
    public static List<JuzcarClass> removeTheIgnoreController(List<JuzcarClass> classList){
        Iterator<JuzcarClass> iterator = classList.iterator();
        JuzcarClass clazzTemp;
        while (iterator.hasNext()){
            clazzTemp = iterator.next();
            if (clazzTemp.getAnnotationMap().keySet().contains("com.ericwyn.juzcar.annotations.JuzcarIgnoreScanner")){
                iterator.remove();
            }
        }
        return classList;
    }

    /**
     * 从 Class 列表当中扫描出 Method, Method 按照 Class 名称分组
     * @param classList
     * @return
     */
    public static HashMap<String,List<JuzcarMethod>> scannerMethods(List<JuzcarClass> classList){
        HashMap<String, List<JuzcarMethod>> methodMap = new HashMap<>();
        for (JuzcarClass juzcarClass : classList){
            Class clazz = juzcarClass.getClazz();
            String key = clazz.getName();
            ArrayList<JuzcarMethod> methodList = new ArrayList<>();
            Method[] methods = clazz.getMethods();

            for (Method method : methods){
                boolean scannerFlag = false;
                boolean ignoreFlag = false;
                Annotation[] annotations = method.getAnnotations();
                for (Annotation an : annotations){
                    if (methodAnnotaions.contains(an.annotationType().getName())){
                        scannerFlag = true;
                    }
                    if (an.annotationType().getName().equals(JuzcarIgnoreScanner.class.getName())){
                        ignoreFlag = true;
                    }
                }
                // 既含有需要被扫描的 flag 又不被 Ignore 标记
                if (scannerFlag && !ignoreFlag){
                    JuzcarMethod juzcarMethod = new JuzcarMethod(method,method.getAnnotations());
                    methodList.add(juzcarMethod);
                }
            }
            methodMap.put(key,methodList);
        }
        return methodMap;
    }

    private static void fetchFileList(File dir,List<File> fileList){
        if(dir.isDirectory()){
            for(File f:dir.listFiles()){
                fetchFileList(f,fileList);
            }
        }else{
            fileList.add(dir);
        }
    }

}
