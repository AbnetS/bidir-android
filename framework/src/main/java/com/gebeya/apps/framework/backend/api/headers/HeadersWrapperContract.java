package com.gebeya.apps.framework.backend.api.headers;

/**
 * Interface for determining what the headers of a request/response could contain in terms of the
 * data in the header.
 */
public interface HeadersWrapperContract {

    /**
     * Get a value from the header (implementation dependent).
     *
     * @param name Value as the <code>key</code> lookup.
     * @return Value as the found <code>value</code> returned from the key.
     */
    String get(String name);
}
