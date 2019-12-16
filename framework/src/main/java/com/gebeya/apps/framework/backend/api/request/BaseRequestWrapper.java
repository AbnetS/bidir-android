package com.gebeya.apps.framework.backend.api.request;

import org.json.JSONObject;

/**
 * Base implementation for the <code>RequestWrapperContract</code> interface.
 *
 * <br />
 * Use this class for quick implementation of the interface itself.
 */
public abstract class BaseRequestWrapper implements RequestWrapperContract {

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";

    public String url;
    public String method;
    public String token;
    public JSONObject json;

    @Override
    public RequestWrapperContract url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public RequestWrapperContract method(String method) {
        this.method = method;
        return this;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public RequestWrapperContract token(String token) {
        this.token = token;
        return this;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public RequestWrapperContract addParam(String name, boolean value) {
        try {
            getJson().accumulate(name, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public RequestWrapperContract addParam(String name, int value) {
        try {
            getJson().accumulate(name, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public RequestWrapperContract addParam(String name, double value) {
        try {
            getJson().accumulate(name, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public RequestWrapperContract addParam(String name, long value) {
        try {
            getJson().accumulate(name, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public RequestWrapperContract addParam(String name, Object value) {
        try {
            getJson().accumulate(name, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public JSONObject getJson() {
        if (json == null)
            json = new JSONObject();
        return json;
    }
}
