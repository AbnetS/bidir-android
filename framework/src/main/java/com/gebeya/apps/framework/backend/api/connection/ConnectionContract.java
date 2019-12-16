package com.gebeya.apps.framework.backend.api.connection;

import android.support.annotation.NonNull;

import com.gebeya.apps.framework.backend.api.request.RequestWrapperContract;
import com.gebeya.apps.framework.backend.api.response.ResponseWrapperContract;

/**
 * Interface for the <code>ConnectionContract</code>. Any other external libraries (such as OkHttp)
 * would need to implement this interface to act as a network connectivity instance.
 */
public interface ConnectionContract {

    /**
     * <code>ConnectivityCallback</code> interface for a <code>ConnectionContract</code> object.
     */
    interface ConnectionCallback {
        /**
         * Called when the connection was successfully established and data downloaded. Implementation
         * is <code>ConnectionContract</code> dependent.
         *
         * @param response
         */
        void onSuccess(ResponseWrapperContract response);

        /**
         * Called when the connection could not be established and no data was downloaded.
         * @param e
         */
        void onFailed(Exception e);
    }

    /**
     * Make a connection to the Internet, with the given parameters.
     *
     * @param request <code>RequestWrapperContract</code> implementation for the request.
     * @param callback  <code>ConnectionCallback</code> implementation for the response.
     */
    void connect(@NonNull RequestWrapperContract request, @NonNull ConnectionCallback callback);
}
