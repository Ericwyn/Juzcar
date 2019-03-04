package com.ericwyn.juzcar.server;

import com.ericwyn.ezerver.SimpleHttpServer;
import com.ericwyn.ezerver.expection.WebServerException;
import com.ericwyn.juzcar.scan.obj.JuzcarApiList;
import com.ericwyn.juzcar.server.handle.ApiHandle;
import com.ericwyn.juzcar.server.handle.PageHandle;
import com.ericwyn.juzcar.utils.JuzcarLogs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Ericwyn on 18-11-26.
 */
public class JuzcarDocServer {

    // juzcar 的临时文件夹
    public static final String JUZCAR_TEMP_DIR = ".juzcar";

    // 从 jar 里提取的静态文件（css\js等）存储的文件夹
    public static final String JAR_STATIC_PATH = JUZCAR_TEMP_DIR +"/" +"static";

    // 从 jar 里提取的页面模板文件的的存储文件夹
    public static final String JAR_TEMPLE_PATH = JUZCAR_TEMP_DIR +"/" +"temple";

    private Map<String, JuzcarApiList> apiMaps;

    public JuzcarDocServer(Map<String, JuzcarApiList> apiMaps) {
        this.apiMaps = apiMaps;
        // 配置静态资源文件夹，将 jar 里面的静态资源文件复制出来，复制到 .juzcar 当中
        try {
            copyStaticFileFromJar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * DOC Server 启动，负责返回页面
     * @param httpPort
     * @param allowDebug
     * @throws WebServerException
     */
    public void startServer(int httpPort, boolean allowDebug) throws WebServerException {
        ApiHandle apiHandle = new ApiHandle(apiMaps);
        PageHandle pageHandle = new PageHandle(apiMaps);

        SimpleHttpServer.Builder builder = new SimpleHttpServer.Builder()
                .setServerPort(httpPort)
                .addHandleMethod(Arrays.asList(
                        apiHandle.apiJsonHandle(),
                        pageHandle.redireRootHandle(),
                        pageHandle.indexPageHandle()
                ))
                .addHandleMethod(pageHandle.apiPageHandles())
                .setWebRoot(".juzcar/static");
        if (allowDebug){
            builder.allowDebug();
        }
        builder.build().start();
    }

    // 将打包得到的 jar 里面的 static 文件夹，复制到 .juzcar 文件夹里面，以此获得 WEBROOT
    private void copyStaticFileFromJar() throws IOException {
        // resource:/media/ericwyn/Work/Chaos/IntiliJ_Java_Project/juzcart/target/classes/
        File staticDir = new File(JUZCAR_TEMP_DIR);
        if (!staticDir.isDirectory()){
            staticDir.mkdirs();
        }
        String path = this.getClass().getClassLoader().getResource("static").getPath();
        if (!path.contains(".jar!")){
            JuzcarLogs.SOUT("NOTE!!!! Juzcar 不是以 JAR 形式导入，可能会无法获取所需静态资源文件");
        } else {
            JuzcarLogs.SOUT("Juzcar 已经成功以 JAR 的形式导入");
        }
        path = path.substring(5);
        path = path.substring(0,path.indexOf(".jar!")+4);
        JarFile jarFile = new JarFile(path);
        Enumeration<JarEntry> entries = jarFile.entries();
        String name ;
        JarEntry jarEntry ;
        while (entries.hasMoreElements()){
            // 创建一个 .juzcar 文件夹，存储从 jar 当中提取出来的 static 当中的静态文件
            jarEntry = entries.nextElement();
            name = jarEntry.toString();
            if (name.startsWith("static") || name.startsWith("temple")){
                if (name.endsWith("/")){
                    File dir = new File(JUZCAR_TEMP_DIR + "/"+name);
                    dir.mkdirs();
                }else {
                    File outputFile = new File(JUZCAR_TEMP_DIR + "/"+name);
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
