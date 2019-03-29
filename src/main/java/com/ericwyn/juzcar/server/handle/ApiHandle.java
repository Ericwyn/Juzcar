package com.ericwyn.juzcar.server.handle;

import com.alibaba.fastjson.JSONObject;
import com.ericwyn.ezerver.handle.HandleMethod;
import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.response.Response;
import com.ericwyn.juzcar.scan.obj.JuzcarApiList;
import com.ericwyn.juzcar.server.out.OutputService;
import com.ericwyn.juzcar.test.JuzcarTestServer;
import com.ericwyn.juzcar.test.obj.JuzcarTestResponse;
import com.ericwyn.juzcar.utils.JuzcarLogs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ericwyn on 18-12-1.
 */
public class ApiHandle {
    private Map<String, JuzcarApiList> apis ;

    public ApiHandle(Map<String, JuzcarApiList> apis){
        this.apis = apis;
    }

    private OutputService outPutService = OutputService.getService();

    /**
     * api 文档导出接口
     *
     * @return
     */
    public HandleMethod apiJsonHandle(){
        return new HandleMethod("/api/outPutHTMLDocument") {
            @Override
            public void requestDo(Request request, Response response) throws IOException {
                File outputHTMLZip = outPutService.getOutputHTMLZip(apis);
                response.sendFileStream(outputHTMLZip);
                response.closeStream();
            }
        };
    }

    /**
     * api 测试接口
     *
     * POST
     * 传入参数
     *      - uri
     *      - paramList
     *          格式是 param name1,param value1; param name2,param value2;
     *
     * @return
     */
    public HandleMethod apiTestHandle(){
        return new HandleMethod("/api/testJuzcarApi") {
            @Override
            public void requestDo(Request request, Response response) throws IOException {
                if (request.getMethod() == Request.METHOD_POST){
                    if (request.getParamMap().get("uri") == null){
                        response.sendTextHtml("请求参数错误, 缺少 uri 参数");
                    } else if (request.getParamMap().get("paramList") == null){
                        response.sendTextHtml("请求参数错误, 缺少 paramList 参数");
                    } else {
                        // 具体测试
                        String uri = request.getParamMap().get("uri").getValue();
                        String paramList = request.getParamMap().get("paramList").getValue();
                        JuzcarLogs.SOUT("收到测试请求, uri 参数：" + uri + "  paramList 参数： " + paramList);
                        HashMap<String, String> params = parseParamListStr(paramList);
                        // 调用测试 Server
                        List<JuzcarTestResponse> responsesList = JuzcarTestServer.getTestServer().testApi(uri, params);
                        String res = "";
                        for (JuzcarTestResponse resp : responsesList){
                            res += resp.toString()+"\n";
                        }
                        // 返回结果
                        response.sendTextHtml(res);
                    }
                    response.closeStream();
                } else {
                    response.sendTextHtml("测试接口需要使用 POST 请求");
                    response.closeStream();
                }
            }
        };
    }

    private HashMap<String, String> parseParamListStr(String paramListStr){
        HashMap<String, String > resMap = new HashMap<>();
        String[] paramStrsTemp = paramListStr.split(";");
        String[] paramTemp;
        for (String str : paramStrsTemp){
            paramTemp = str.split(",");
            if (paramTemp.length == 2){
                resMap.put(paramTemp[0], paramTemp[1]);
            }
        }
        return resMap;
    }

}
