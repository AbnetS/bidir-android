package com.gebeya.apps.framework.util;

/**
 * Interface representing any object that can (and should probably be) logged
 */
public interface Loggable {
    /**
     * Show a simple log message, by calling {@link android.util.Log#d(String, String)}
     */
    void d(String message);

    /**
     * Show a simple log message, by calling {@link android.util.Log#d(String, String)} with a custom
     * <code>formatArts</code> for the output, by using {@link String#format(String, Object...)}.
     */
    void d(String message, Object... formatArgs);

    /**
     * Show a simple error log message, by calling {@link android.util.Log#e(String, String)}
     */
    void e(String message);

    /**
     * Show a simple error log message, by calling {@link android.util.Log#e(String, String)} with a custom
     * <code>formatArts</code> for the output, by using {@link String#format(String, Object...)}.
     */
    void e(String message, Object... formatArgs);
}
