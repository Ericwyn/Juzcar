package com.ericwyn.juzcar.scan.obj;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * API 的抽象类
 *      name        api 的 name， 来自 RequestMapping 注解
 *      url         api 的 url，来自同上
 *      method      api 的 method，来自同上，或分析 PostMapping/GetMapping 获得
 *      params      api 的 params，来自 @RequestMapping 注解的 Method 里，那些被 @RequestParam 注解的参数的分析
 *      type        api 的类型，分为两类，JSON 或者 PAGE，分别对应的是返回 JSON 数据的接口或者返回页面的接口
 *
 * Created by Ericwyn on 18-11-26.
 */
public class JuzcarApi {
    private String name;
    private String[] url;
    private String[] method;
    private List<JuzcarParam> params;
    private ApiType type;

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

    public ApiType getType() {
        return type;
    }

    public void setType(ApiType type) {
        this.type = type;
    }
}
