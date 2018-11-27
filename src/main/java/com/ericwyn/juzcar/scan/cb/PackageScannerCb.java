package com.ericwyn.juzcar.scan.cb;

import java.io.File;

/**
 * Created by Ericwyn on 18-11-22.
 */
public interface PackageScannerCb {
    void callback(File file, Class<?> clazz);
}
