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

    public List<HandleMethod> apiPageHandles(){
        ArrayList<HandleMethod> handleMethods = new ArrayList<>();
        String requestUri;

        HttpTemple apiPageTemple = new HttpTemple(TempleUtils.getTemple("controller"));
        for (String key : apis.keySet()){
            requestUri = TempleUtils.getUriFromApiKey(key);
            // 匹配 /{requestUri}.html 或者 /{requestUri}
            handleMethods.add(new HandleMethod("([/]){0,1}"+requestUri+"+([.]+[h]+[t]+[m]+[l]){0,1}", true) {
                @Override
                public void requestDo(Request request, Response response) throws IOException {
                    String apiPageHTML = apiPageTemple.clearReplace()
                            .replace(TempleKey.API_Nav, TempleUtils.getNavTemple(apis))
                            .replace(TempleKey.API_PackgeName, key)
                            .replace(TempleKey.API_ControllerNote, apis.get(key).getClazz().getNote())
                            .replace(TempleKey.API_ApiList, TempleUtils.getApiListTemple(apis.get(key)))
                            .string();
                    response.sendTextHtml(apiPageHTML);
                    response.closeStream();
                }
            });
        }
        // 匹配 /index.html 或者 /index
        handleMethods.add(new HandleMethod("([/]){0,1}index+([.]+[h]+[t]+[m]+[l]){0,1}", true) {
            @Override
            public void requestDo(Request request, Response response) throws IOException {
                HttpTemple temple = new HttpTemple(TempleUtils.getTemple("index"));
                response.sendTextHtml(temple
                        .replace(TempleKey.INDEX_README, "欢迎使用 Juzcar ，一个简单、快捷的 Web API 文档自动生成工具。")
                        .replace(TempleKey.INDEX_Nav, TempleUtils.getNavTemple(apis))
                        .string());
                response.closeStream();
            }
        });
        return handleMethods;
    }

}
