package com.ericwyn.juzcar.scan.obj;

/**
 * JuzcarServer 的启动配置
 *
 * Created by Ericwyn on 19-3-3.
 */
public class JuzcarConfig {
    // 是否开启 Ezerver http debug 日志
    private boolean httpLog = false;
    // 是否开启 juzcarServer 日志
    private boolean juzcarServerLog = true;
    // Doc Server 的 Ezerver http 服务器的运行端口
    private int httpPort = 9696;

    public JuzcarConfig(){

    }

    public JuzcarConfig allowHttpLog(){
        this.httpLog = true;
        return this;
    }

    public JuzcarConfig allowJuzcarLog(){
        this.juzcarServerLog = true;
        return this;
    }

    public JuzcarConfig setHttpPort(int port){
        this.httpPort = port;
        return this;
    }

    public static JuzcarConfig defaultJuzcarConfig(){
        return new JuzcarConfig();
    }

    public int getHttpPort() {
        return httpPort;
    }

    public boolean isHttpLog() {
        return httpLog;
    }

    public boolean isJuzcarServerLog() {
        return juzcarServerLog;
    }
}
