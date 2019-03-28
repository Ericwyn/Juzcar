package com.ericwyn.juzcar.test.http;

import com.ericwyn.juzcar.scan.obj.JuzcarApi;
import com.ericwyn.juzcar.test.request.JuzcarTestRequest;
import com.ericwyn.juzcar.test.request.JuzcarTestResponse;

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
public class JuzcarRequestUtils {

    // ms
    private static final int defalutConnectTimeOut = 1000 * 60 ;
    private static final int defaultReadTimeOut = 1000 * 60 ;
    private static final String defaultCharset = "UTF-8";

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

    /**
     * 发送请求
     * @param request
     * @param apiUri
     * @param method
     * @return
     */
    private static JuzcarTestResponse sendRequest(JuzcarTestRequest request, String apiUri, String method){
        JuzcarTestResponse response = new JuzcarTestResponse(request);
        response.setMethod(method);
        response.setRequest(request);

        HashMap<String, Object> testRespTemp = null;

        switch (method){
            case "POST":
                testRespTemp = postRequest(request, apiUri);
                break;
            case "GET":
                testRespTemp = getRequest(request, apiUri);
                break;
            case "DELETE":
                // TODO
                break;
            case "PUT":
                // TODO
                break;
            case "HEAD":
                // TODO
                break;
        }
        // 设置返回的  respText
        if (testRespTemp != null && testRespTemp.get("respText") != null) {
            response.setRespText(testRespTemp.get("respText").toString());
        }
        // TODO 设置返回的 status

        return response;
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
        String respText = HttpRequest.post(url, params, defalutConnectTimeOut, defaultReadTimeOut, defaultCharset);
        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("respText", respText);
        return resMap;
    }

    /**
     * 对 GET 请求的具体封装，返回 Map 有两个 key, 一个是 statusCode， 一个是 respText
     * @param request
     * @param apiUri
     * @return
     */
    private static HashMap<String, Object> getRequest(JuzcarTestRequest request, String apiUri){
        String url = (request.getTestServerHost()+"/"+apiUri).replaceAll("//","/");
        HashMap<String, String> params = request.getRequestParam();
        String respText = HttpRequest.get(url, params, defalutConnectTimeOut, defaultReadTimeOut, defaultCharset);
        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("respText", respText);
        return resMap;
    }
}
