package com.ericwyn.juzcar.server.handle;

import com.ericwyn.ezerver.handle.HandleMethod;
import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.response.Response;
import com.ericwyn.juzcar.scan.obj.JuzcarApiList;
import com.ericwyn.juzcar.server.tml.HttpTemple;
import com.ericwyn.juzcar.server.tml.TempleKey;
import com.ericwyn.juzcar.server.tml.TempleUtils;

import java.io.IOException;
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
                HttpTemple navItemTemple = new HttpTemple(TempleUtils.getTemple("navItem"));
                String nav = "";
                for (String key : apis.keySet()){
                    navItemTemple.clearReplace();
                    navItemTemple.replace(TempleKey.NAVITEM_NAME, apis.get(key).getClazz().getNote());
                    navItemTemple.replace(TempleKey.NAVITEM_PACKAGENAME, key.replaceAll("\\.","_"));

                    nav += navItemTemple.string()+"\n";
                }
                // 渲染 README
                temple.replace(TempleKey.INDEX_README, "README");
                // 渲染 INDEX
                temple.replace(TempleKey.INDEX_NAV, nav);

                response.sendTextHtml(temple.string());
                response.closeStream();
            }
        };
    }

//    public HandleMethod apiPageHandle(Map<String, JuzcarApiList> apis){
//
//    }

}
