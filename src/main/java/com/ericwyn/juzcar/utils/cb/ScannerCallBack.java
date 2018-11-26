package com.ericwyn.juzcar.utils.cb;

import java.io.File;

/**
 * Created by Ericwyn on 18-11-22.
 */
public interface ScannerCallBack {
    void callback(File file, Class<?> clazz);
}
