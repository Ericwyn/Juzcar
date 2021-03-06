package com.ericwyn.juzcar.scan;

import com.ericwyn.juzcar.config.PackageName;
import com.ericwyn.juzcar.scan.analysis.ApiAnalysis;
import com.ericwyn.juzcar.scan.annotations.JuzcarClassNote;
import com.ericwyn.juzcar.scan.annotations.JuzcarMethodNote;
import com.ericwyn.juzcar.scan.cb.PackageScannerCb;
import com.ericwyn.juzcar.scan.annotations.JuzcarIgnoreScanner;
import com.ericwyn.juzcar.scan.cb.ApiAnalysisCb;
import com.ericwyn.juzcar.scan.obj.JuzcarApi;
import com.ericwyn.juzcar.scan.obj.JuzcarApiList;
import com.ericwyn.juzcar.scan.obj.JuzcarClass;
import com.ericwyn.juzcar.scan.obj.JuzcarMethod;
import com.ericwyn.juzcar.scan.obj.JuzcarMethodList;
import com.ericwyn.juzcar.utils.JuzcarLogs;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by Ericwyn on 18-11-25.
 */
public class ScannerUtils {

    // 应该再写一层分析工具，分析工具是一个 Map<String, Fun> ，然后下面的这些 List，就是这些 Map 的 ketSet
    private static List<String> shouldAnalysisClass = Arrays.asList(
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


    /**
     * 扫描 package 的方法，
     * @param iPackage  第一个 package 是包的名称
     * @param callback  callBack 处理扫描得到的(类文件名，类文件 Class)
     */
    private static void scanPackage(String iPackage, PackageScannerCb callback){
        String path = iPackage.replace(".","/");
        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        try {
            // 这里确认 url 是一个文件的位置
            if (url.toString().startsWith("file")){
                JuzcarLogs.SOUT("扫描的文件 : "+url.toString());
                int packageNameStart = url.toString().indexOf(path);
                if (System.getProperty("os.name").toLowerCase().startsWith("window")){
                    packageNameStart--;
                }
                String filePath = URLDecoder.decode(url.getFile(),"utf-8");
                File dir = new File(filePath);
                List<File> fileList = new ArrayList<>();
                fetchFileList(dir,fileList);
                for (File f:fileList){
                    String fileName = f.getAbsolutePath();
                    if(fileName.endsWith("class")){
                        String fileUrl = "file:"+fileName;
                        // 后面减去 6 减去的是 ".class"
                        String nosuffixFileName ;
                        if (fileUrl.endsWith(".class")){
                            nosuffixFileName = fileUrl.substring(packageNameStart, fileUrl.length()-6);
                        } else {
                            nosuffixFileName = fileUrl;
                        }
                        String filePackage = nosuffixFileName
                                .replaceAll("/", ".")
                                .replaceAll("\\\\",".");
                        Class<?> clazz = Class.forName(filePackage);
                        // 扫描得到了 Class 之后
                        callback.callback(f,clazz);
                    }else {
                        // 扫描的是非 Class
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
     * scanPackage 的话是扫描这个package 里面所有的类， CB 的话负责对每个类具体的处理
     *
     * PackageScannerCb 的第一个参数是这个 class 的文件，第二个参数是这个类的具体 Class
     * @param initClass
     * @return
     */
    public static List<JuzcarClass> scannerAllController(Class initClass){
        ArrayList<JuzcarClass> res = new ArrayList<>();
        // 下面的代码相当于一个 for 循环
        // 循环这个 initClass 所在的 package 及其子目录的 package
        // 得到一个 class 之后就对其进行 CB 当中的处理
        scanPackage(initClass.getPackage().getName(), new PackageScannerCb() {
            @Override
            public void callback(File file, Class<?> clazz) {
                Annotation[] annotations = clazz.getAnnotations();
                boolean isJuzcarClassFlag = false;
                String juzcarClassNote = "";

                for (Annotation an : annotations){
                    // 如果 Class 中包含了与 Controller 有关的 注解
                    if (shouldAnalysisClass.contains(an.annotationType().getName())){
                       isJuzcarClassFlag = true;
                    }
                    // 获取备注的值
                    if (an instanceof JuzcarClassNote){
                        juzcarClassNote = ((JuzcarClassNote) an).value();
                    }
                }
                if (isJuzcarClassFlag){
                    res.add(new JuzcarClass(clazz, annotations, juzcarClassNote));
                }
            }
        });
        // 确定类当中哪些是真的要扫描的（去掉被 Ignore 注解的类）
        return removeTheIgnoreController(res);
    }

    /**
     * 清除被 com.ericwyn.juzcar.scan.annotations.JuzcarIgnoreScanner 注解的 Class
     * @param classList
     * @return
     */
    private static List<JuzcarClass> removeTheIgnoreController(List<JuzcarClass> classList){
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
    public static HashMap<String, JuzcarMethodList> scannerMethods(List<JuzcarClass> classList){

        HashMap<String, JuzcarMethodList> methodMap = new HashMap<>();
        JuzcarMethodList juzcarMethodListTemp;
        for (JuzcarClass juzcarClass : classList){
            Class clazz = juzcarClass.getClazz();
            String key = clazz.getName();
            ArrayList<JuzcarMethod> methodList = new ArrayList<>();
            Method[] methods = clazz.getMethods();

            for (Method method : methods){
                boolean scannerFlag = false;
                boolean ignoreFlag = false;
                Annotation[] annotations = method.getAnnotations();

                String juzcarMethodNote = "";
                for (Annotation an : annotations){
                    if (methodAnnotaions.contains(an.annotationType().getName())){
                        scannerFlag = true;
                    }
                    if (an.annotationType().getName().equals(JuzcarIgnoreScanner.class.getName())){
                        ignoreFlag = true;
                    }
                    // 获取备注的值
                    if (an instanceof JuzcarMethodNote){
                        juzcarMethodNote = ((JuzcarMethodNote)an).value();
                    }
                }
                // 既含有需要被扫描的 flag 又不被 Ignore 标记
                if (scannerFlag && !ignoreFlag){
                    JuzcarMethod juzcarMethod = new JuzcarMethod(method,method.getAnnotations(), juzcarMethodNote);
                    methodList.add(juzcarMethod);
                }
            }
            juzcarMethodListTemp = new JuzcarMethodList();
            juzcarMethodListTemp.setClazz(juzcarClass);
            juzcarMethodListTemp.setMethods(methodList);
            methodMap.put(key,juzcarMethodListTemp);
        }
        return methodMap;
    }

    /**
     * 从已经分组好的 JuzcarMethod 里面扫描出 JuzcarApi，方便后续处理
     *
     * @param methodMap
     * @return
     */
    public static HashMap<String, JuzcarApiList> scannerAPI(HashMap<String, JuzcarMethodList> methodMap){
        HashMap<String, JuzcarApiList> apiMap = new HashMap<>();
        JuzcarApiList juzcarApiList;
        JuzcarMethodList methodListTemp;
        for (String packageName : methodMap.keySet()){
            methodListTemp = methodMap.get(packageName);
            List<JuzcarApi> apiList = new ArrayList<>();
            List<JuzcarMethod> methodList = methodListTemp.getMethods();
            for (JuzcarMethod juzcarMethod : methodList){
                // 分析 method 里面的注解，对 Api 注解进行分析并且返回 JuzcarApi 对象，将 Method 的注解变成一个个 API
                for (String anName : juzcarMethod.getAnnotationMap().keySet()){
                    // 如果这个 method 里面包含一些特定的注解例如 @RequestMapping 的话
                    // 就要对其进行处理，解析出 JuzcarApi
                    if (methodAnnotaions.contains(anName)){
                        ApiAnalysisCb apiAnalysis = apiAnalysisMap.get(anName);
                        Annotation annotation = juzcarMethod.getAnnotationMap().get(anName);
                        JuzcarApi api = apiAnalysis.analysis(juzcarMethod,annotation);
                        apiList.add(api);
//                        methodList.add(apiAnalysisMap.get(anName).analysis(method.getAnnotationMap().get(anName)))
                    }
                }
            }
            juzcarApiList = new JuzcarApiList();
            juzcarApiList.setApis(apiList);
            juzcarApiList.setMethods(methodListTemp);
            apiMap.put(packageName, juzcarApiList);
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
