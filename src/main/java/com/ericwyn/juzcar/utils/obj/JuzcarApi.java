package com.ericwyn.juzcar.utils.obj;

/**
 *
 * API 的抽象类
 *
 * Created by Ericwyn on 18-11-26.
 */
public class JuzcarApi {
    private String[] url;
    private String[] method;

    public JuzcarApi() {

    }

    public String[] getMethod() {
        return method;
    }

    public void setMethod(String[] method) {
        this.method = method;
    }

    public String[] getUrl() {
        return url;
    }

    public void setUrl(String[] url) {
        this.url = url;
    }
}
