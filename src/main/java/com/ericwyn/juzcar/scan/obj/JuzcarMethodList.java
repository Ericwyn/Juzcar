package com.ericwyn.juzcar.scan.obj;

import java.util.List;

/**
 * 用来存储扫描得到的 JuzcarMethod 的 List
 *
 * Created by Ericwyn on 19-3-3.
 */
public class JuzcarMethodList {
    // 来源 Class
    private JuzcarClass clazz;
    // 得到的 Method
    private List<JuzcarMethod> methods;

    public JuzcarClass getClazz() {
        return clazz;
    }

    public void setClazz(JuzcarClass clazz) {
        this.clazz = clazz;
    }

    public List<JuzcarMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<JuzcarMethod> methods) {
        this.methods = methods;
    }
}
