package com.ericwyn.juzcar.scan;

import com.ericwyn.juzcar.config.PackageName;
import com.ericwyn.juzcar.scan.analysis.ApiAnalysis;
import com.ericwyn.juzcar.scan.cb.PackageScannerCb;
import com.ericwyn.juzcar.scan.annotations.JuzcarIgnoreScanner;
import com.ericwyn.juzcar.scan.cb.ApiAnalysisCb;
import com.ericwyn.juzcar.scan.obj.JuzcarApi;
import com.ericwyn.juzcar.scan.obj.JuzcarClass;
import com.ericwyn.juzcar.scan.obj.JuzcarMethod;

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
import java.util.Set;

/**
 * Created by Ericwyn on 18-11-25.
 */
public class ScannerUtils {

    // 应该再写一层分析工具，分析工具是一个 Map<String, Fun> ，然后下面的这些 List，就是这些 Map 的 ketSet
    private static List<String> classAnnotaions = Arrays.asList(
            PackageName.Controller,
            PackageName.RestController
    );

    // 创建一个 回调方法的 map 用来对包含特定注解的 Map 进行处理
    // 因为要确保对 Spring 框架无依赖，所以这里对注解的处理不能直接将注解转换成已有注解类，而只能通过反射获取注解的get方法，再获取值
    private static HashMap<String, ApiAnalysisCb>  apiAnalysisMap = new HashMap<String, ApiAnalysisCb>(){
        {
            put(PackageName.RequestMapping, ApiAnalysis.org_springframework_web_bind_annotation_RequestMapping);
            put(PackageName.PostMapping, ApiAnalysis.org_springframework_web_bind_annotation_PostMapping);
            put(PackageName.GetMapping, ApiAnalysis.org_springframework_web_bind_annotation_GetMapping);
        }
    };

    private static Set<String> methodAnnotaions = apiAnalysisMap.keySet();


    // 包扫描
    private static void scanPackage(String iPackage, PackageScannerCb callback){
        String path = iPackage.replace(".","/");
        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        try {
            // 这里确认 url 是一个文件的位置
            if (url.toString().startsWith("file")){
                System.out.println(url.toString());
                System.out.println(path);
                System.out.println(url.toString().indexOf(path));
                int packageNameStart = url.toString().indexOf(path);
                System.out.println(url.toString().substring(url.toString().indexOf(path)));
                String filePath = URLDecoder.decode(url.getFile(),"utf-8");
                File dir = new File(filePath);
                List<File> fileList = new ArrayList<>();
                fetchFileList(dir,fileList);
                for (File f:fileList){
                    String fileName = f.getAbsolutePath();
                    if(fileName.endsWith("class")){
                        // 文件名一般为 /media/ericwyn/Work/Chaos/IntiliJ_Java_Project/juzcart/target/classes/com/ericwyn/juzcart/JuzcartApplication.class
                        // 找到 "classes" 的位置，这个 classes 后面第8个位置开始的后面的文件路径，就是包名字

//                        String nosuffixFileName = fileName.substring(8+fileName.lastIndexOf("classes"),fileName.indexOf(".class"));
                        String nosuffixFileName = ("file:"+fileName).substring(packageNameStart);
                        String filePackage = nosuffixFileName.replaceAll("/", ".");
                        Class<?> clazz = Class.forName(filePackage);
                        callback.callback(f,clazz);
                    }else {
                        callback.callback(f,null);
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
        scanPackage(initClass.getPackage().getName(), new PackageScannerCb() {
            @Override
            public void callback(File file, Class<?> clazz) {
                Annotation[] annotations = clazz.getAnnotations();
                for (Annotation an : annotations){
                    // 如果 Class 中包含了与 Controller 有关的 注解
                    if (classAnnotaions.contains(an.annotationType().getName())){
                        res.add(new JuzcarClass(clazz, annotations));
                        break;
                    }
                }
            }
        });
        return res;
    }

    /**
     * 清除被 com.ericwyn.juzcar.scan.annotations.JuzcarIgnoreScanner 注解的 Class
     * @param classList
     * @return
     */
    public static List<JuzcarClass> removeTheIgnoreController(List<JuzcarClass> classList){
        Iterator<JuzcarClass> iterator = classList.iterator();
        JuzcarClass clazzTemp;
        while (iterator.hasNext()){
            clazzTemp = iterator.next();
            if (clazzTemp.getAnnotationMap().keySet().contains(JuzcarIgnoreScanner.class.getName())){
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

    /**
     * 从已经分组好的 JuzcarMethod 里面扫描出 JuzcarApi，方便后续处理
     *
     * @param methodMap
     * @return
     */
    public static HashMap<String, List<JuzcarApi>> scannerAPI(HashMap<String, List<JuzcarMethod>> methodMap){
        HashMap<String, List<JuzcarApi>> apiMap = new HashMap<>();
        for (String packageName : methodMap.keySet()){
            List<JuzcarApi> apiList = new ArrayList<>();
            List<JuzcarMethod> methodList = methodMap.get(packageName);
            for (JuzcarMethod method : methodList){
                // 分析 method 里面的注解，对 Api 注解进行分析并且返回 JuzcarApi 对象，将 Method 的注解变成一个个 API
                for (String anName : method.getAnnotationMap().keySet()){
                    if (methodAnnotaions.contains(anName)){
                        ApiAnalysisCb apiAnalysis = apiAnalysisMap.get(anName);
                        Annotation annotation = method.getAnnotationMap().get(anName);
                        JuzcarApi api = apiAnalysis.analysis(method.getMethod(),annotation);
                        apiList.add(api);
//                        methodList.add(apiAnalysisMap.get(anName).analysis(method.getAnnotationMap().get(anName)))
                    }
                }
            }
            apiMap.put(packageName, apiList);
        }
        return apiMap;
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
