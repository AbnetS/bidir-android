package com.gebeya.apps.framework.backend.api.request;

import org.json.JSONObject;

/**
 * Contract representing a request object. The implementation depends on the HTTP Library (such as
 * OkHttp) being used as the transport of your data to the network.
 */
public interface RequestWrapperContract {

    /**
     * Adds a <code>URL</code> to the request object.
     *
     * @param url <code>URL</code> parameter.
     * @return <code>RequestWrapperContract</code> object for chaining.
     */
    RequestWrapperContract url(String url);

    /**
     * Retrieve the <code>URL</code>
     *
     * @return The <code>URL</code>
     */
    String getUrl();

    /**
     * Add a HTTP request method
     *
     * @param method HTTP Method (GET, PUT, POST, DELETE)
     * @return <code>RequestWrapperContract</code> object for chaining.
     * @see BaseRequestWrapper#METHOD_GET
     * @see BaseRequestWrapper#METHOD_POST
     * @see BaseRequestWrapper#METHOD_PUT
     * @see BaseRequestWrapper#METHOD_DELETE
     */
    RequestWrapperContract method(String method);

    /**
     * Return the HTTP request method.
     *
     * @return HTTP request method (GET, PUT, POST, DELETE)
     */
    String getMethod();

    /**
     * Add a JWT (JSON Web Token) into the header. This can be replaced via header interceptors,
     * such as in OkHttp.
     *
     * @param token JWT Token received from the server
     * @return <code>RequestWrapperContract</code> object for chaining.
     */
    RequestWrapperContract token(String token);

    /**
     * Return the JWT token.
     * @return JWT Token received from the server.
     */
    String getToken();

    /**
     * Add a <code>boolean</code> parameter value.
     *
     * @param name <code>String</code> name value.
     * @param value <code>boolean</code> parameter value.
     * @return <code>RequestWrapperContract</code> object for chaining.
     */
    RequestWrapperContract addParam(String name, boolean value);

    /**
     * Add a <code>int</code> parameter value.
     *
     * @param name <code>String</code> name value.
     * @param value <code>int</code> parameter value.
     * @return <code>RequestWrapperContract</code> object for chaining.
     */
    RequestWrapperContract addParam(String name, int value);

    /**
     * Add a <code>double</code> parameter value.
     *
     * @param name <code>String</code> name value.
     * @param value <code>double</code> parameter value.
     * @return <code>RequestWrapperContract</code> object for chaining.
     */
    RequestWrapperContract addParam(String name, double value);

    /**
     * Add a <code>long</code> parameter value.
     *
     * @param name <code>String</code> name value.
     * @param value <code>long</code> parameter value.
     * @return <code>RequestWrapperContract</code> object for chaining.
     */
    RequestWrapperContract addParam(String name, long value);

    /**
     * Add a <code>Object</code> parameter value.
     *
     * @param name <code>String</code> name value.
     * @param value <code>Object</code> parameter value.
     * @return <code>RequestWrapperContract</code> object for chaining.
     */
    RequestWrapperContract addParam(String name, Object value);

    /**
     * Combines together all the key/value parameters given and retrieves back a <code>JSON</code>
     * object.
     *
     * @return <code>JSON</code> object of all the addParam(name, value) data combined.
     */
    JSONObject getJson();
}
