package com.ericwyn.juzcar.test;

import com.ericwyn.juzcar.scan.obj.JuzcarApi;
import com.ericwyn.juzcar.scan.obj.JuzcarApiList;
import com.ericwyn.juzcar.test.request.JuzcarTestRequest;
import com.ericwyn.juzcar.test.request.JuzcarTestResponse;
import com.ericwyn.juzcar.utils.JuzcarLogs;
import com.ericwyn.juzcar.utils.SpringBootUtils;

import java.util.HashMap;

/**
 *
 * Juzcar 测试, 包含了对 api 的测试
 *
 * Created by Ericwyn on 19-3-23.
 */
public class JuzcarTestServer {
    private static JuzcarTestServer juzcarTestServer = null;
    private HashMap<String, JuzcarApiList> apis;

    private String testServerHost;

    /**
     * TestServer 的启动类, 会返回一个 Server 供测试
     * @param apis
     * @param args
     * @return
     */
    public static JuzcarTestServer startServer(HashMap<String, JuzcarApiList> apis, String[] args){
        // 解析服务器地址
        String host = parseTestServerHost(args);
        JuzcarTestServer testServer = new JuzcarTestServer(apis, host);
        juzcarTestServer = testServer;
        return testServer;
    }

    public static JuzcarTestServer getTestServer(){
        if (juzcarTestServer == null){
            JuzcarLogs.SOUT("Test Server 尚未创建, 可能会导致接口无法测试, 请先调用 JuzcarTestServer.startServer(apis, args) 方法");
        }
        return juzcarTestServer;
    }


    private JuzcarTestServer(HashMap<String, JuzcarApiList> apis, String host){
        this.apis = apis;
        this.testServerHost = host;
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
        request.setTestServerHost(this.testServerHost);
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

    /**
     * 解析测试服务器的 host
     * @return
     */
    private static String parseTestServerHost(String[] args){
        SpringBootUtils utils = SpringBootUtils.getUtils(args);
        return "127.0.0.1:" + utils.getSpringBootServerPort();
    }
}
