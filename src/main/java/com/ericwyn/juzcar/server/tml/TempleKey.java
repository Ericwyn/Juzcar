package com.ericwyn.juzcar.server.tml;

/**
 * 存储模板文件当中需要替换的 Key，便于统一管理
 * 命名规则如下
 *
 *      INDEX_README --> index 模板的 readme 的 key
 *
 * Created by Ericwyn on 19-3-4.
 */
public class TempleKey {
    // 首页的 README
    public static final String INDEX_README = "README";
    public static final String INDEX_Nav = "Nav";
    // 导航项
    public static final String NAVITEM_Name = "Name";
    public static final String NAVITEM_PackageName = "PackageName";

    // API 详情页面
    public static final String API_PackgeName = "PackageName";
    public static final String API_Nav = "Nav";
    public static final String API_ApiList = "ApiList";
    public static final String API_ControllerNote = "ControllerNote";

    // API List
    public static final String APIITEM_ApiName ="ApiName";
    public static final String APIITEM_ApiNote="ApiNote";
    public static final String APIITEM_ApiURL ="ApiUri";
    public static final String APIITEM_ApiMethod ="ApiMethod";
    public static final String APIITEM_ParamItemList ="ParamItemList";

    // Param List
    public static final String PARAMLIST_ParamName="ParamName";
    public static final String PARAMLIST_ParamType="ParamType";
    public static final String PARAMLIST_ParamRequire="ParamRequire";
    public static final String PARAMLIST_ParamNote="ParamNote";

}
