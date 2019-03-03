package com.ericwyn.juzcar.scan.obj;

import java.util.List;

/**
 * 用来保存最终扫描到的 Controller 以及其下的全部 JuzcarApi
 * Created by Ericwyn on 19-3-3.
 */
public class JuzcarApiList {
    // 扫描得到的 apis
    private List<JuzcarApi> apis;
    // 扫描得到的 methods
    private JuzcarMethodList methods;

    public JuzcarClass getClazz() {
        return methods.getClazz();
    }

    public List<JuzcarApi> getApis() {
        return apis;
    }

    public void setApis(List<JuzcarApi> apis) {
        this.apis = apis;
    }

    public JuzcarMethodList getMethods() {
        return methods;
    }

    public void setMethods(JuzcarMethodList methods) {
        this.methods = methods;
    }
}
