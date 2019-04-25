package com.ericwyn.juzcar.server.tml;

import com.ericwyn.juzcar.scan.obj.JuzcarApi;
import com.ericwyn.juzcar.scan.obj.JuzcarApiList;
import com.ericwyn.juzcar.scan.obj.JuzcarParam;
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
    private static HttpTemple apiListTemple = new HttpTemple(TempleUtils.getTemple("apiItem"));
    private static HttpTemple paramListTemple = new HttpTemple(TempleUtils.getTemple("paramItem"));

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
        String navName;
        String[] split;
        for (String key : apis.keySet()){
            navName = apis.get(key).getClazz().getNote();
            if (navName.trim().equals("")){
                split = key.split(".");
                navName = split[split.length-1];
            }
            navItemTemple.clearReplace()
                    .replace(TempleKey.NAVITEM_Name, navName)
                    .replace(TempleKey.NAVITEM_PackageName, getUriFromApiKey(key)+".html");

            nav += navItemTemple.string()+"\n";
        }
        return nav;
    }

    /**
     * 渲染 API 列表当中各个 API 的参数列表
     * @param api
     * @return
     */
    public static String getParamListTemple(JuzcarApi api){
        String resTml = "";
        for (JuzcarParam param : api.getParams()){
            if (param == null){
                continue;
            }
            resTml += paramListTemple.clearReplace()
                    .replace(TempleKey.PARAMLIST_ParamName, param.getName())
                    .replace(TempleKey.PARAMLIST_ParamNote, param.getNote())
                    .replace(TempleKey.PARAMLIST_ParamRequire, param.getRequired() != null && param.getRequired()?"是":"否")
                    .replace(TempleKey.PARAMLIST_ParamType, param.getType().getName())
                    .replace(TempleKey.PARAMLIST_ParamValue, param.getDefaultValue())
                    .string();
        }
        return resTml;
    }

    /**
     * 渲染 API 列表
     * @param apiList
     * @return
     */
    public static String getApiListTemple(JuzcarApiList apiList){
        String resTml = "";
        for (JuzcarApi api : apiList.getApis()){
            apiListTemple.clearReplace();
            resTml += apiListTemple.clearReplace()
                    .replace(TempleKey.APIITEM_ApiNote, api.getApiNote())
                    .replace(TempleKey.APIITEM_ApiName, api.getName())
                    .replace(TempleKey.APIITEM_ApiMethod, api.getMethodString())
                    .replace(TempleKey.APIITEM_ApiURL, api.getUrlString())
                    .replace(TempleKey.APIITEM_ParamItemList, getParamListTemple(api))
                    .replace(TempleKey.APIITEM_ApiNote, api.getApiNote())
                    .string();
        }
        return resTml;
    }

    public static String getUriFromApiKey(String key){
        return key.replaceAll("\\.","_");
    }
}
