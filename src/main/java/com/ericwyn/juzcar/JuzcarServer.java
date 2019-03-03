package com.ericwyn.juzcar;

import com.ericwyn.ezerver.expection.WebServerException;
import com.ericwyn.juzcar.scan.ScannerUtils;
import com.ericwyn.juzcar.scan.cb.JuzcarScannerCb;
import com.ericwyn.juzcar.scan.obj.JuzcarApiList;
import com.ericwyn.juzcar.scan.obj.JuzcarClass;
import com.ericwyn.juzcar.scan.obj.JuzcarConfig;
import com.ericwyn.juzcar.scan.obj.JuzcarMethodList;
import com.ericwyn.juzcar.server.JuzcarDocServer;
import com.ericwyn.juzcar.utils.JuzcarLogs;

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
    private Class initClass;

    private HashMap<String, JuzcarApiList> apis;

    private JuzcarServer(Class initClass, HashMap<String, JuzcarApiList> apis) {
        this.initClass = initClass;
        this.apis = apis;
    }

    /**
     * 启动函数，cb 用来查看那些扫描得到的 api
     *
     * @param initClass
     * @param juzcarScanner
     */
    public static void run(Class initClass, JuzcarScannerCb juzcarScanner, JuzcarConfig config){
        // 启动的线程
        Runnable juzcarRunnable = new Runnable() {
            @Override
            public void run() {
                // scan 模块
                List<JuzcarClass> juzcarClasses = ScannerUtils.scannerAllController(initClass);
                // 确定类当中哪些是真的要扫描的（去掉被 Ignore 注解的类）
                ScannerUtils.removeTheIgnoreController(juzcarClasses);
                // 以 Controller 名称分组，扫描其中的方法
                HashMap<String, JuzcarMethodList> juzcarMethodListMap = ScannerUtils.scannerMethods(juzcarClasses);
                // 针对方法扫描出具体的 API
                HashMap<String, JuzcarApiList> apis = ScannerUtils.scannerAPI(juzcarMethodListMap);

                JuzcarServer juzcarServer = new JuzcarServer(initClass, apis);
                // 回调这个 JuzcarServer 到外面去
                juzcarScanner.callback(juzcarServer);

//            System.out.println(JSONObject.toJSONString(apis));
//            System.out.println(juzcarClasses.size());

                // Doc server 模块
                try {
                    new JuzcarDocServer(apis).startServer(config.getHttpPort(), config.isHttpLog());
                } catch (WebServerException e) {
                    e.printStackTrace();
                }


                // TODO server模块，静态页面存储问题
                // TODO Json 模块导入问题
                // 对 Controller 和 RestController 的分别判断问题
                // TODO 对 RequestBody 和 ResponseBody 的支持
                // TODO 如果交流的方式并非 JSON 而是 XML ？（暂时不考虑支持 xml 了
                // TODO 增加一些注解能够强硬设置API 的信息，注解在方法，替换掉自动分析注解得到的 api 信息
                // TODO 增加一个注解来设置 Controller 的名字，注解在类，避免 Controller 名字太长

                // TODOO server 默认不开启日志,使用 debug Flag 开启日志
            }
        };

        new Thread(juzcarRunnable).run();
    }

    /**
     * 默认启动函数， 缺省的 cb 和缺省 JuzcarConfig
     * @param initClass
     */
    public static void run(Class initClass){
        run(initClass, defaultScannerCb, JuzcarConfig.defaultJuzcarConfig());
    }


    /**
     * 启动函数， 缺省的 cb, 自定义 JuzcarConfig
     * @param initClass
     */
    public static void run(Class initClass, JuzcarConfig config){
        run(initClass, defaultScannerCb, config);
    }

    /**
     * 启动函数， 自定义的 cb, 缺省 JuzcarConfig
     * @param initClass
     */
    public static void run(Class initClass, JuzcarScannerCb scannerCb){
        run(initClass, scannerCb, JuzcarConfig.defaultJuzcarConfig());
    }


    /**
     * 缺省 CB
     */
    private static JuzcarScannerCb defaultScannerCb = new JuzcarScannerCb() {
        @Override
        public void callback(JuzcarServer server) {
            HashMap<String, JuzcarApiList> apis = server.getApis();
            for (String key : apis.keySet()){
                JuzcarLogs.SOUT("扫描 "+ key+" 得到 "+apis.get(key).getApis().size()+" 个接口");
            }
        }
    };

    public Class getInitClass() {
        return initClass;
    }

    public HashMap<String, JuzcarApiList> getApis() {
        return apis;
    }
}
