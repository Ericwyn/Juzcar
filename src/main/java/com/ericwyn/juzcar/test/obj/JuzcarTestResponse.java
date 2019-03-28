package com.ericwyn.juzcar.test.obj;

/**
 * API 测试返回结果
 *
 * Created by Ericwyn on 19-3-23.
 */
public class JuzcarTestResponse {
    private JuzcarTestRequest request;
    private String method;
    private String respText;
    private Integer statusCode;

    public JuzcarTestResponse(JuzcarTestRequest request) {
        this.request = request;
    }

    public JuzcarTestRequest getRequest() {
        return request;
    }

    public void setRequest(JuzcarTestRequest request) {
        this.request = request;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRespText() {
        return respText;
    }

    public void setRespText(String respText) {
        this.respText = respText;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "JuzcarTestResponse{" +
                "request=" + request +
                ", method='" + method + '\'' +
                ", respText='" + respText + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}
