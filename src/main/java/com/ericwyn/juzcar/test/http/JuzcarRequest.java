package com.ericwyn.juzcar.test.http;

import com.ericwyn.juzcar.scan.obj.JuzcarApi;
import com.ericwyn.juzcar.test.request.JuzcarTestRequest;
import com.ericwyn.juzcar.test.request.JuzcarTestResponse;
import com.sun.deploy.net.HttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * 存放 API 测试 Http 工具
 *
 * startRequest 方法对 JuacarTestRequest 进行调用
 *
 * Created by Ericwyn on 19-3-27.
 */
public class JuzcarRequest {

    /**
     * 因为一个 API 可能会有多个 uri 和多个 method 的支持，所以需要进行多次测试
     *
     * 得到的是多个测试的结果
     * @param request
     * @return
     */
    public static List<JuzcarTestResponse> startRequest(JuzcarTestRequest request){
        JuzcarApi juzcarApi = request.getJuzcarApi();
        List<JuzcarTestResponse> resList = new ArrayList<>();
        for (String apiUri : juzcarApi.getUrl()){
            for (String method : juzcarApi.getMethod()){
                JuzcarTestResponse response = sendRequest(request, apiUri, method);
                resList.add(response);
            }
        }
        return resList;
    }

    /**
     * 只对一次 API进行一次测试
     * @param request
     * @param apiUri
     * @param method
     * @return
     */
    public static JuzcarTestResponse startRequest(JuzcarTestRequest request, String apiUri, String method){
        JuzcarTestResponse response = sendRequest(request, apiUri, method);
        return response;
    }

    private static JuzcarTestResponse sendRequest(JuzcarTestRequest request, String apiUri, String method){
        JuzcarTestResponse response = new JuzcarTestResponse(request);
        response.setMethod(method);
        response.setRequest(request);

        HashMap<String, Object> testRespTemp;
        switch (method){
            case "POST":
                testRespTemp = postRequest(request, apiUri);
                break;
            case "GET":

                break;
            case "DELETE":

                break;
            case "PUT":

                break;
            case "HEAD":

                break;
        }
        return null;
    }

    /**
     * 对 POST 请求的具体封装，返回 Map 有两个 key, 一个是 statusCode， 一个是 respText
     * @param request
     * @param apiUri
     * @return
     */
    private static HashMap<String , Object> postRequest(JuzcarTestRequest request, String apiUri){
        String url = (request.getTestServerHost()+"/"+apiUri).replaceAll("//","/");
        HashMap<String, String> params = request.getRequestParam();
        HttpRequest.post(url, params,  1000 * 60, 1000 * 60, "UTF-8");
        return null;
    }
}
