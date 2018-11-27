package com.ericwyn.juzcar.scan.cb;

import com.ericwyn.juzcar.scan.obj.JuzcarApi;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Ericwyn on 18-11-27.
 */
public interface JuzcarScannerCb {
    void callback(HashMap<String, List<JuzcarApi>> apis);
}
