package com.ericwyn.juzcar.test.request;

import com.ericwyn.juzcar.scan.obj.JuzcarApi;

import java.util.HashMap;

/**
 *
 * 一个 API 测试请求
 *
 * Created by Ericwyn on 19-3-23.
 */
public class JuzcarTestRequest {
    // 需要测试的 Api
    private JuzcarApi juzcarApi;

    // 测试的自定义参数
    private HashMap<String, String> requestParam;

    public JuzcarTestRequest() {
    }

    public JuzcarTestResponse test(){
        // TODO 具体的测试的逻辑
        return null;
    }


    public JuzcarApi getJuzcarApi() {
        return juzcarApi;
    }

    public void setJuzcarApi(JuzcarApi juzcarApi) {
        this.juzcarApi = juzcarApi;
    }

    public HashMap<String, String> getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(HashMap<String, String> requestParam) {
        this.requestParam = requestParam;
    }
}
