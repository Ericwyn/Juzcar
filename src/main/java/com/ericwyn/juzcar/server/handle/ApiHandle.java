package com.ericwyn.juzcar.server.handle;

import com.alibaba.fastjson.JSONObject;
import com.ericwyn.ezerver.handle.HandleMethod;
import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.response.Response;
import com.ericwyn.juzcar.scan.obj.JuzcarApiList;
import com.ericwyn.juzcar.server.out.OutputService;

import java.io.File;
import java.io.IOException;
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

}
