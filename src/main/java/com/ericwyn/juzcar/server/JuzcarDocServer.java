package com.ericwyn.juzcar.server;

import com.ericwyn.ezerver.SimpleHttpServer;
import com.ericwyn.ezerver.expection.WebServerException;
import com.ericwyn.juzcar.scan.obj.JuzcarApi;
import com.ericwyn.juzcar.server.handle.ApiHandle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Ericwyn on 18-11-26.
 */
public class JuzcarDocServer {

    private Map<String, List<JuzcarApi>> apiMaps;

    public JuzcarDocServer(Map<String, List<JuzcarApi>> apiMaps) {
        this.apiMaps = apiMaps;
        // 配置静态资源文件夹，将 jar 里面的静态资源文件复制出来，复制到 .juzcar 当中
        try {
            copyStaticFileFromJar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() throws WebServerException {
        ApiHandle apiHandle = new ApiHandle();

        SimpleHttpServer server = new SimpleHttpServer.Builder()
                .setServerPort(9696)
                .addHandleMethod(apiHandle.apiJsonHandle(apiMaps))
                .setWebRoot(".juzcar/static")
                .allowDebug()
                .build();
        server.start();
    }

    // 将打包得到的 jar 里面的 static 文件夹，复制到 .juzcar 文件夹里面，以此获得 WEBROOT
    private void copyStaticFileFromJar() throws IOException {
        // resource:/media/ericwyn/Work/Chaos/IntiliJ_Java_Project/juzcart/target/classes/
        File staticDir = new File(".juzcar");
        if (!staticDir.isDirectory()){
            staticDir.mkdirs();
        }
        String path = this.getClass().getClassLoader().getResource("static").getPath();
        if (!path.contains(".jar!")){
            System.out.println("NOTE!!!! Juzcar 不是以 JAR 形式导入，可能会无法获取所需静态资源文件");
        }
        path = path.substring(5);
        path = path.substring(0,path.indexOf(".jar!")+4);
        JarFile jarFile = new JarFile(path);
        Enumeration<JarEntry> entries = jarFile.entries();
        String name ;
        JarEntry jarEntry ;
        while (entries.hasMoreElements()){
            jarEntry = entries.nextElement();
            name = jarEntry.toString();
            if (name.startsWith("static")){
                if (name.endsWith("/")){
                    File dir = new File(".juzcar/"+name);
                    dir.mkdirs();
                }else {
                    File outputFile = new File(".juzcar/"+name);
                    InputStream inputStream = jarFile.getInputStream(jarEntry);
                    OutputStream outputStream = new FileOutputStream(outputFile);
                    int length = -1;
                    byte[] bytes = new byte[2048];
                    while ((length = inputStream.read(bytes)) != -1){
                        outputStream.write(bytes,0,length);
                        outputStream.flush();
                    }
                    inputStream.close();
                    outputStream.close();
                }
            }
        }
    }
}
