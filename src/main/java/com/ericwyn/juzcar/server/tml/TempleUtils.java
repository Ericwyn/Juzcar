package com.ericwyn.juzcar.server.tml;

import com.ericwyn.juzcar.server.JuzcarDocServer;

import java.io.File;

/**
 *
 * 这个TempleUtils 是一个独立的模板渲染工具，主要使用的方法就是 String.replace()
 *
 * 模板的语法主要是  {{需要替换的名称}},
 *
 * Created by Ericwyn on 19-3-4.
 */
public class TempleUtils {
    private static File templeDir = new File(JuzcarDocServer.JAR_TEMPLE_PATH);

    /**
     * 通过模板名称获取模板的文件
     *
     * @param templeName
     * @return
     */
    public static File getTemple(String templeName){
        File res = null;
        if (templeName.endsWith(".html")){
            res = new File(templeDir, templeName);
        } else {
            res =  new File(templeDir, templeName+".html");
        }
        if ( res.isFile()){
            return res;
        } else {
            return null;
        }
    }

}
