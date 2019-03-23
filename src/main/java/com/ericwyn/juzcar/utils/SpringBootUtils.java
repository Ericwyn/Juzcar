package com.ericwyn.juzcar.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * Spring Boot 相关的工具, 包括配置的读取之类的
 *
 * Created by Ericwyn on 19-3-23.
 */
public class SpringBootUtils {
    private static SpringBootUtils springBootUtils;


    private final String PROPERTIES_CONFIG_FILE_NAME = "application.properties";
    private final String YAML_CONFIG_FILE_NAME = "application.properties";

    // 配置文件获取的配置
    private HashMap<String, String> springBootConfig = null;
    // java 启动命令获取的配置
    private HashMap<String, String> javaRunParamConfig = null;

    private String[] startArgs;


    private SpringBootUtils(){

    }

    public static SpringBootUtils getUtils(){
        return getUtils(new String[]{});
    }

    /**
     * 单例模式获取工具类, 并且解析 SpringBoot 配置文件
     * @return
     */
    public static SpringBootUtils getUtils(String[] args){
        if (springBootUtils == null){
            synchronized (SpringBootUtils.class) {
                if (springBootUtils == null){
                    // 解析配置文件
                    springBootUtils = new SpringBootUtils();
                    springBootUtils.readSpringBootConfigure();
                    // 解析 java 启动参数
                    springBootUtils.startArgs = args;
                    springBootUtils.parseJavaRunParams();
                }
            }
        }
        return springBootUtils;
    }

    /**
     * 获取 Spring Boot 服务器运行接口
     * @return
     */
    public String getSpringBootServerPort(){
        String key = "server.port";
        String port = "8080";
        if (javaRunParamConfig != null && javaRunParamConfig.get(key) != null){
            port = javaRunParamConfig.get(key);
        } else if (springBootConfig != null && springBootConfig.get(key) != null){
            port = springBootConfig.get(key);
        }
        return port;
    }

    /**
     *
     * 自己实现的对 Spring Boot 配置文件的读取, 分别读的是下面的这些文件
     *
     *      当前目录下的/config目录
     *      当前目录
     *      classpath里的/config目录
     *      classpath 跟目录
     *
     *      java 启动命令 -server.port=1000
     *
     * @return
     */
    private void readSpringBootConfigure(){
        //
        File configFile = null;
        if ((configFile = readCurrentDirConfig()) != null
            || (configFile = readCurrentConfig()) != null
            || (configFile = readClassPathDirConfig()) != null
            || (configFile = readClassPathConfig()) != null){

            if (configFile.getName().equals("properties")){
                this.springBootConfig = parsePropertiesConfigFile(configFile);
            } else if (configFile.getName().endsWith("yml")){
                this.springBootConfig = parseYamlConfigFile(configFile);
            } else {
                this.springBootConfig = new HashMap<>();
            }
        }
    }

    /**
     * 解析 application.properties 文件
     * @param configFile
     * @return
     */
    private HashMap<String, String> parsePropertiesConfigFile(File configFile){
        // 名字判断
//        if (!configFile.getName().equals(PROPERTIES_CONFIG_FILE_NAME)){
//            return null;
//        }

        HashMap<String, String> resMap = new HashMap<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));
            String line;
            String[] splitTemp ;
            if ((line = bufferedReader.readLine()) != null){
                if (line.indexOf("=") > 0){
                    splitTemp = line.split("=");
                    resMap.put(splitTemp[0], line.replace(splitTemp[0], "").replace("=", "").trim());
                }
            }
            bufferedReader.close();
            return resMap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析 application.yml 文件
     * @param configFile
     * @return
     */
    private HashMap<String, String> parseYamlConfigFile(File configFile){
        // TODO YAML 的解析
        return null;
    }

    /**
     *
     * 读取当前目录下的 config/
     *
     * @return
     */
    private File readCurrentDirConfig(){
        File configFile = new File("config/" + PROPERTIES_CONFIG_FILE_NAME);
        if (configFile.isFile()){
            return configFile;
        } else {
            configFile = new File("config/" + YAML_CONFIG_FILE_NAME);
            if (configFile.isFile()){
                return configFile;
            }
        }
        return null;
    }

    /**
     * 当前目录下的 config
     * @return
     */
    private File readCurrentConfig(){
        File configFile = new File(PROPERTIES_CONFIG_FILE_NAME);
        if (configFile.isFile()){
            return configFile;
        } else {
            configFile = new File(YAML_CONFIG_FILE_NAME);
            if (configFile.isFile()){
                return configFile;
            }
        }
        return null;
    }

    /**
     * classpath里的/config目录
     * @return
     */
    private File readClassPathDirConfig(){
        // TODO classPath /config
        return null;
    }

    /**
     * classpath 根目录
     * @return
     */
    private File readClassPathConfig(){
        // TODO classPath 根目录
        return null;
    }

    /**
     * 解析 Java 启动参数, 启动 args
     * @return
     */
    private void parseJavaRunParams(){
        this.javaRunParamConfig = new HashMap<>();
        String[] splitTemp ;
        for (String arg : startArgs){
            if (arg.startsWith("--") && arg.contains("=")){
                splitTemp = arg.split("=");
                this.javaRunParamConfig.put(splitTemp[0].replace("--", ""), splitTemp[1]);
            }
        }
    }
}
