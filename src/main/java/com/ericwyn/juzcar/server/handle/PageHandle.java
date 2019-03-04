package com.ericwyn.juzcar.server.handle;

import com.ericwyn.ezerver.handle.HandleMethod;
import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.response.Response;
import com.ericwyn.juzcar.scan.obj.JuzcarApiList;
import com.ericwyn.juzcar.server.tml.HttpTemple;
import com.ericwyn.juzcar.server.tml.TempleKey;
import com.ericwyn.juzcar.server.tml.TempleUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 负责页面元素的返回
 *
 * Created by Ericwyn on 19-3-4.
 */
public class PageHandle {
    private Map<String, JuzcarApiList> apis;
    public PageHandle(Map<String, JuzcarApiList> apis){
        this.apis = apis;
    }

    public HandleMethod redireRootHandle(){
        return new HandleMethod("") {
            @Override
            public void requestDo(Request request, Response response) throws IOException {
                response.sendRedirect302("index");
                response.closeStream();
            }
        };
    }


    public HandleMethod indexPageHandle(){
        return new HandleMethod("index") {
            @Override
            public void requestDo(Request request, Response response) throws IOException {
                HttpTemple temple = new HttpTemple(TempleUtils.getTemple("index"));
                // 渲染导航栏

                // 渲染 README
                temple.replace(TempleKey.INDEX_README, "README");
                // 渲染 INDEX
                temple.replace(TempleKey.INDEX_NAV, TempleUtils.getNavTemple(apis));

                response.sendTextHtml(temple.string());
                response.closeStream();
            }
        };
    }

    public List<HandleMethod> apiPageHandles(){
        ArrayList<HandleMethod> handleMethods = new ArrayList<>();
        String requestUri;

        HttpTemple apiPageTemple = new HttpTemple(TempleUtils.getTemple("controller"));
        for (String key : apis.keySet()){
            requestUri = TempleUtils.getUriFromApiKey(key);
            handleMethods.add(new HandleMethod(requestUri) {
                @Override
                public void requestDo(Request request, Response response) throws IOException {
                    apiPageTemple.clearReplace();
                    apiPageTemple.replace(TempleKey.API_NAV, TempleUtils.getNavTemple(apis));
                    apiPageTemple.replace(TempleKey.API_PACKAGENAME, key);
                    String apiPageHTML = apiPageTemple.string();
                    response.sendTextHtml(apiPageHTML);
                    response.closeStream();
                }
            });
        }
        return handleMethods;
    }

}
