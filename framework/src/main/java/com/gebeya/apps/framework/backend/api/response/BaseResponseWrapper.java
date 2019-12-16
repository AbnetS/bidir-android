package com.gebeya.apps.framework.backend.api.response;

/**
 * Base implementation of the <code>ResponseWrapperContract</code> interface. Use this as your
 * base class for a simple API response, but also have an implementation for the underlying HTTP
 * transport library (such as OkHttp) under the hood.
 */
public abstract class BaseResponseWrapper implements ResponseWrapperContract {

    public int code;
    public String body;

    @Override
    public boolean isOk() {
        return code >= 200 && code < 300;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
