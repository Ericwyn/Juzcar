package com.ericwyn.juzcar.utils;

import com.ericwyn.juzcar.utils.obj.JuzcarClass;
import com.ericwyn.juzcar.utils.obj.JuzcarMethod;
import com.ericwyn.juzcart.JuzcartApplication;

import java.util.HashMap;
import java.util.List;

/**
 *
 * Juzcar 服务入口类
 *      扫描接口
 *      生成 config.json
 *      生成配置文件
 *
 * Created by Ericwyn on 18-11-26.
 */
public class JuzcarServer {
    public static void run(Class initClass){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<JuzcarClass> juzcarClasses = ScannerUtils.scannerAllController(JuzcartApplication.class);
                // 确定类当中哪些是真的要扫描的（去掉被 Ignore 注解的类
                ScannerUtils.removeTheIgnoreController(juzcarClasses);
                // 以 Controller 名称分组，扫描其中的方法
                HashMap<String, List<JuzcarMethod>> juzcarMethodMap = ScannerUtils.scannerMethods(juzcarClasses);
                // 针对方法扫描出具体的 API
                System.out.println(juzcarClasses.size());
            }
        }).run();
    }
}
