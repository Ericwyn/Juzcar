package com.ericwyn.juzcar.server.config;

/**
 * Created by Ericwyn on 19-3-6.
 */
public class JuzcarDocConfig {
    public static final String JUZCAR_TEMP_DIR = ".juzcar";

    // 从 jar 里提取的静态文件（css\js等）存储的文件夹
    public static final String JAR_STATIC_PATH = JUZCAR_TEMP_DIR +"/" +"static";

    // 从 jar 里提取的页面模板文件的的存储文件夹
    public static final String JAR_TEMPLE_PATH = JUZCAR_TEMP_DIR +"/" +"temple";

    public static final String JUZCAR_OUT_TEMP_DIR = JUZCAR_TEMP_DIR + "/" + "outTemp";
}
