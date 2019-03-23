package com.ericwyn.juzcar.test;

import com.ericwyn.juzcar.scan.obj.JuzcarApi;
import com.ericwyn.juzcar.scan.obj.JuzcarApiList;
import com.ericwyn.juzcar.test.request.JuzcarTestRequest;
import com.ericwyn.juzcar.test.request.JuzcarTestResponse;

import java.util.HashMap;

/**
 *
 * Juzcar 测试, 包含了对 api 的测试
 *
 * Created by Ericwyn on 19-3-23.
 */
public class JuzcarTestServer {
    private HashMap<String, JuzcarApiList> apis;

    public JuzcarTestServer(HashMap<String, JuzcarApiList> apis){
        this.apis = apis;
    }

    /**
     * 传入一个 url, 进行测试
     * @param apiUrl
     * @param requestParam
     * @return
     */
    public JuzcarTestResponse testApi(String apiUrl, HashMap<String, String> requestParam){
        JuzcarApi trueApi = null;
        // api url 鉴定是否正确,  TODO 双重循环优化
        for (String key : apis.keySet()){
            for (JuzcarApi api : apis.get(key).getApis()){
                if (api.getUrlString().contains(apiUrl)){
                    trueApi = api;
                    break;
                }
            }
        }
        if (trueApi != null){
            return testApi(trueApi, requestParam);
        } else {
            return null;
        }
    }

    /**
     * 传入一个 api, 进行测试
     * @param api
     * @return
     */
    public JuzcarTestResponse testApi(JuzcarApi api, HashMap<String, String> requestParam){
        JuzcarTestRequest request = new JuzcarTestRequest();
        request.setJuzcarApi(api);
        request.setRequestParam(requestParam);
        return testApi(request);
    }

    /**
     * 传入一个 testRequest 进行测试
     * @param testRequest
     * @return
     */
    public JuzcarTestResponse testApi(JuzcarTestRequest testRequest){
        return testRequest.test();
    }
}
