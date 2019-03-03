package com.ericwyn.juzcar.server.handle;

import com.alibaba.fastjson.JSONObject;
import com.ericwyn.ezerver.handle.HandleMethod;
import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.response.Response;
import com.ericwyn.juzcar.scan.obj.JuzcarApiList;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Ericwyn on 18-12-1.
 */
public class ApiHandle {
    public HandleMethod apiJsonHandle(Map<String, JuzcarApiList> apis){
        return new HandleMethod("/api") {
            @Override
            public void requestDo(Request request, Response response) throws IOException {
                response.sendJsonData(JSONObject.toJSONString(apis));
                response.closeStream();
            }
        };
    }

}
