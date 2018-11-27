package com.ericwyn.juzcar.server;

import com.ericwyn.ezerver.SimpleHttpServer;
import com.ericwyn.ezerver.expection.WebServerException;
import com.ericwyn.ezerver.handle.HandleMethod;
import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.response.Response;
import com.ericwyn.juzcar.scan.obj.JuzcarApi;
//import com.ericwyn.juzcar.utils.JsonUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Ericwyn on 18-11-26.
 */
public class JuzcarDocServer {

    private Map<String, List<JuzcarApi>> apiMaps;



    public JuzcarDocServer(Map<String, List<JuzcarApi>> apiMaps) {
        this.apiMaps = apiMaps;
    }

    public void startServer() throws WebServerException {
        HandleMethod getApiJsonMethod = new HandleMethod("/api") {
            @Override
            public void requestDo(Request request, Response response) throws IOException {
//                String json = JsonUtils.getMapper().writeValueAsString(apiMaps);
//                response.sendJsonData(json);
                response.closeStream();
            }
        };

        SimpleHttpServer server = new SimpleHttpServer.Builder()
                .setServerPort(9696)
                .addHandleMethod(getApiJsonMethod)
                .setWebRoot("/media/ericwyn/Work/Chaos/IntiliJ_Java_Project/juzcart/src/main/java/com/ericwyn/juzcar/server/webRoot")
                .allowDebug()
                .build();
        server.start();
    }

}
