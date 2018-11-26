package com.ericwyn.juzcar.scan.obj;

/**
 * Juzcar Api 当中，接口的请求参数
 *
 * Created by Ericwyn on 18-11-26.
 */
public class JuzcarParam {
    private String name;
    private String value;
    private String defaultValue;
    private Boolean required;
    private Class type;

    public JuzcarParam() {
    }

    public JuzcarParam(String name, String value, String defaultValue, Boolean required) {
        this.name = name;
        this.value = value;
        this.defaultValue = defaultValue;
        this.required = required;
    }

    public JuzcarParam(String name, String value, String defaultValue, Boolean required, Class type) {
        this.name = name;
        this.value = value;
        this.defaultValue = defaultValue;
        this.required = required;
        this.type = type;
    }

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

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}
