package com.ericwyn.juzcar.scan.cb;

import java.io.File;

/**
 * Created by Ericwyn on 18-11-22.
 */
public interface PackageScannerCb {
    /**
     * 该 CB 作为package 扫描的得到文件之后的处理 CB
     * @param file 扫描得到文件的名字
     * @param clazz
     */
    void callback(File file, Class<?> clazz);
}
