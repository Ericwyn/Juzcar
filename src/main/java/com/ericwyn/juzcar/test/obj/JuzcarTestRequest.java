package com.ericwyn.juzcar.test.obj;

import com.ericwyn.juzcar.scan.obj.JuzcarApi;
import com.ericwyn.juzcar.test.http.JuzcarRequestUtils;

import java.util.HashMap;
import java.util.List;

/**
 *
 * 一个 API 测试请求， 调用 test 方法，完成测试
 *
 * Created by Ericwyn on 19-3-23.
 */
public class JuzcarTestRequest {
    // 需要测试的 Api
    private JuzcarApi juzcarApi;
    private String testServerHost;
    // 测试的自定义参数
    private HashMap<String, String> requestParam;

    public JuzcarTestRequest(JuzcarApi juzcarApi, String testServerHost, HashMap<String, String> requestParam) {
        this.juzcarApi = juzcarApi;
        this.testServerHost = testServerHost;
        this.requestParam = requestParam;
    }

    /**
     * 测试所有可能的 API，包括 api uri 和 method
     * @return
     */
    public List<JuzcarTestResponse> test(){
        List<JuzcarTestResponse> responses = JuzcarRequestUtils.startRequest(this);
        return responses;
    }

    public JuzcarTestResponse test(String uri, String method){
        JuzcarTestResponse responses = JuzcarRequestUtils.startRequest(this, uri, method);
        return responses;
    }

    public String getTestServerHost() {
        return testServerHost;
    }

    public void setTestServerHost(String testServerHost) {
        this.testServerHost = testServerHost;
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
