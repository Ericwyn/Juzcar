package com.ericwyn.juzcart;

import com.ericwyn.ezerver.expection.WebServerException;
import com.ericwyn.juzcar.scan.obj.ReturnType;
import com.ericwyn.juzcar.scan.obj.JuzcarApi;
import com.ericwyn.juzcar.server.JuzcarDocServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ericwyn on 19-1-21.
 */
public class JuzcarDocServerTest {
    public static void main(String[] args) throws WebServerException {
        Map<String, List<JuzcarApi>> apiMaps = new HashMap<>();
        ArrayList<JuzcarApi> apiList;

        apiList = new ArrayList<>();
        for (int i =0;i<10;i++){
            JuzcarApi api = new JuzcarApi();
            api = new JuzcarApi();
            api.setReturnType(ReturnType.JSON);
            api.setUrl(new String[]{"/api/v1/aaa"+i});
            api.setMethod(new String[]{"POST"});
            api.setParams(null);
            apiList.add(api);
        }
        apiMaps.put("aaaController", apiList);


        apiList = new ArrayList<>();
        for (int i =0;i<10;i++){
            JuzcarApi api = new JuzcarApi();
            api = new JuzcarApi();
            api.setReturnType(ReturnType.STRING);
            api.setUrl(new String[]{"/api/v1/bbb"+i});
            api.setMethod(new String[]{"GET"});
            api.setParams(null);
            apiList.add(api);
        }
        apiMaps.put("bbbController", apiList);

        apiList = new ArrayList<>();
        for (int i =0;i<10;i++){
            JuzcarApi api = new JuzcarApi();
            api = new JuzcarApi();
            api.setReturnType(ReturnType.STRING);
            api.setUrl(new String[]{"/api/v1/ccc"+i});
            api.setMethod(new String[]{"POST"});
            api.setParams(null);
            apiList.add(api);
        }
        apiMaps.put("cccController", apiList);

        JuzcarDocServer server = new JuzcarDocServer(apiMaps);
        server.startServer();
    }

}
