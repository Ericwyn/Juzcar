package com.ericwyn.juzcar.server.tml;

import com.ericwyn.juzcar.scan.obj.JuzcarApiList;
import com.ericwyn.juzcar.server.JuzcarDocServer;

import java.io.File;
import java.util.Map;

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
    private static HttpTemple navItemTemple = new HttpTemple(TempleUtils.getTemple("navItem"));

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

    /**
     * 渲染出一个导航栏，多个页面共用所以提取出来了
     * @param apis
     * @return
     */
    public static String getNavTemple(Map<String, JuzcarApiList> apis){
        String nav = "";
        for (String key : apis.keySet()){
            navItemTemple.clearReplace();
            navItemTemple.replace(TempleKey.NAVITEM_NAME, apis.get(key).getClazz().getNote());
            navItemTemple.replace(TempleKey.NAVITEM_PACKAGENAME, getUriFromApiKey(key));

            nav += navItemTemple.string()+"\n";
        }
        return nav;
    }

    public static String getUriFromApiKey(String key){
        return key.replaceAll("\\.","_");
    }
}
