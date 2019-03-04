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
    public static final String INDEX_NAV = "Nav";
    // 导航项
    public static final String NAVITEM_NAME = "Name";
    public static final String NAVITEM_PACKAGENAME = "PackageName";

    // API 详情页面
    public static final String API_PACKAGENAME = "PackageName";
    public static final String API_CONTROLLERNAME = "ControllerName";
    public static final String API_NAV = "Nav";

}
