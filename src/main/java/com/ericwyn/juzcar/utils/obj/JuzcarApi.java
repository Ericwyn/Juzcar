package com.ericwyn.juzcar.utils.obj;

import java.util.List;

/**
 *
 * API 的抽象类
 *
 * Created by Ericwyn on 18-11-26.
 */
public class JuzcarApi {
    private String[] url;
    private String[] method;
    private List<JuzcarParam> params;

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

    public List<JuzcarParam> getParams() {
        return params;
    }

    public void setParams(List<JuzcarParam> params) {
        this.params = params;
    }
}
