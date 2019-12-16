package com.gebeya.apps.framework.backend.api.response;

import com.gebeya.apps.framework.backend.api.headers.HeadersWrapperContract;

/**
 * Interface representing the response returned from the API. This should be implemented by the
 * transport library (such as OkHttp) or some other implementation.
 */
public interface ResponseWrapperContract {

    /**
     * Set the headers via any object implementing the <code>HeadersWrapperContract</code>
     * implementation.
     *
     * @param wrapper <code>HeadersWrapperContract</code> implementation.
     */
    void setHeaders(HeadersWrapperContract wrapper);

    /**
     * Return the <code>HeadersWrapperContract</code> implementation.
     *
     * @return <code>HeadersWrapperContract</code> implementation.
     */
    HeadersWrapperContract getHeaders();

    /**
     * Returns whether the connection was successful, between 200 and 300 (inclusive).
     *
     * @return <code>boolean</code> indicating whether connection was successful or not.
     */
    boolean isOk();

    /**
     * Set the body of the response.
     */
    void setBody(String body);

    /**
     * Retrieve the body.
     */
    String getBody();

    /**
     * Set the status code.
     */
    void setCode(int code);

    /**
     * Retrieve the status code.
     */
    int getCode();
}