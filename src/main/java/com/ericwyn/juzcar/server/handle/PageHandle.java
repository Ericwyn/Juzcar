package com.ericwyn.juzcar.server.handle;

import com.ericwyn.ezerver.handle.HandleMethod;
import com.ericwyn.ezerver.request.Request;
import com.ericwyn.ezerver.response.Response;
import com.ericwyn.juzcar.scan.obj.JuzcarApiList;

import java.io.IOException;
import java.util.Map;

/**
 * 负责页面元素的返回
 *
 * Created by Ericwyn on 19-3-4.
 */
public class PageHandle {
    public HandleMethod redireRootHandle(){
        return new HandleMethod("") {
            @Override
            public void requestDo(Request request, Response response) throws IOException {
                response.sendRedirect302("index");
                response.closeStream();
            }
        };
    }

    public HandleMethod indexPageHandle(Map<String, JuzcarApiList> apis){
        return new HandleMethod("index") {
            @Override
            public void requestDo(Request request, Response response) throws IOException {
                response.sendTextHtml("你好啊");
                response.closeStream();
            }
        };
    }

}
