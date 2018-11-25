package com.ericwyn.juzcar.utils;

import com.ericwyn.juzcar.IWhat;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ericwyn on 18-11-25.
 */
public class PackageUtils {

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
    public static List<Class> scannerAllController(Class initClass){
        ArrayList<Class> res = new ArrayList<>();
        scanPackage(initClass.getPackage().getName(), new IWhat() {
            @Override
            public void execute(File file, Class<?> clazz) {
                Annotation[] annotations = clazz.getAnnotations();
                for (Annotation an : annotations){
                    if (an.annotationType().getName().contains("org.springframework.stereotype.Controller")){
                        res.add(clazz);
                    }else if (an.annotationType().getName().contains("org.springframework.web.bind.annotation.RestController")){
                        res.add(clazz);
                    }
                }
            }
        });
        return res;
    }

    private static void  fetchFileList(File dir,List<File> fileList){
        if(dir.isDirectory()){
            for(File f:dir.listFiles()){
                fetchFileList(f,fileList);
            }
        }else{
            fileList.add(dir);
        }
    }

}
