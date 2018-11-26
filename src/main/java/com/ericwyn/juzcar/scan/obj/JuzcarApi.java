package com.ericwyn.juzcar.scan.obj;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * API 的抽象类
 *
 * Created by Ericwyn on 18-11-26.
 */
public class JuzcarApi {
    private String name;
    private String[] url;
    private String[] method;
    private List<JuzcarParam> params;

    public JuzcarApi() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (this.params == null){
            this.params = new ArrayList<>();
        }
        return params;
    }

    public void setParams(List<JuzcarParam> params) {
        this.params = params;
    }
}
