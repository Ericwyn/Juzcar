package com.ericwyn.juzcar.utils.obj;

/**
 * Juzcar Api 当中，接口的请求参数
 *
 * Created by Ericwyn on 18-11-26.
 */
public class JuzcarParam {
    private String name;
    private String defaultValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
