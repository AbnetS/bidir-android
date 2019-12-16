package com.gebeya.mobile.bidir.impl.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.gebeya.mobile.bidir.BuildConfig;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;

import java.util.List;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;
import io.reactivex.Observable;

public final class Utils {

    private static final String TAG = "LOS-BIDIR-DEBUG";

    private static final int LOG_MAX_LENGTH = 4000;

    public static synchronized void d(Object tag, String message) {
        splitLog(tag.getClass().getSimpleName(), message, false);
    }

    public static synchronized void d(String tag, String message) {
        splitLog(tag, message, false);
    }

    public static synchronized void d(Object tag, String message, Object... formatArgs) {
        splitLog(tag.getClass().getSimpleName(), String.format(Locale.getDefault(), message, formatArgs), false);
    }

    public static synchronized void d(String tag, String message, Object... formatArgs) {
        splitLog(tag, String.format(Locale.getDefault(), message, formatArgs), false);
    }

    public static synchronized void e(Object tag, String message) {
        splitLog(tag.getClass().getSimpleName(), message, true);
    }

    public static synchronized void e(String tag, String message) {
        splitLog(tag, message, true);
    }

    public static synchronized void e(Object tag, String message, Object... formatArgs) {
        splitLog(tag.getClass().getSimpleName(), String.format(Locale.getDefault(), message, formatArgs), true);
    }

    public static synchronized void e(String tag, String message, Object... formatArgs) {
        splitLog(tag, String.format(Locale.getDefault(), message, formatArgs), true);
    }

    private static synchronized void splitLog(String tag, String message, boolean isError) {
        if (message == null || message.length() == 0) return;
        int length = message.length();
        for (int i = 0; i < length; i += LOG_MAX_LENGTH) {
            int end = i + LOG_MAX_LENGTH < length ? i + LOG_MAX_LENGTH : length;
            String segment = message.substring(i, end);
            boolean release = BuildConfig.BUILD_TYPE.equals("release");
            if (isError) {
                if (release) {
                    Crashlytics.log(String.format(Locale.getDefault(), "ERR - %s : %s", tag, segment));
                } else {
                    Crashlytics.log(Log.ERROR, TAG, String.format(Locale.getDefault(), "%s : %s", tag, segment));
                }
            } else {
                if (release) {
                    Crashlytics.log(String.format(Locale.getDefault(), "%s : %s", tag, segment));
                } else {
                    Crashlytics.log(Log.DEBUG, TAG, String.format(Locale.getDefault(), "%s : %s", tag, segment));
                }
            }
        }
    }

    public static String formatName(@NonNull String firstName,
                                    @NonNull String middleName,
                                    @NonNull String lastName) {
        final boolean hasMiddle = !middleName.trim().isEmpty() && !middleName.equals("-");

        return String.format(Locale.getDefault(),
                "%s %s%s",
                firstName,
                hasMiddle ? (middleName + " ") : "",
                lastName
        );
    }

    public static void openKeyboard(@NonNull EditText input) {
        final Context context = input.getContext();
        if (context == null) return;
        final InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager == null) return;
        manager.toggleSoftInputFromWindow(
                input.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0
        );
    }

    public static void initializeFabric(Context context) {
        boolean sendCrashLogs = Boolean.parseBoolean(BuildConfig.SEND_CRASH_LOGS);
        Crashlytics crashlytics = sendCrashLogs ? new Crashlytics() :
                new Crashlytics.Builder()
                        .core(new CrashlyticsCore.Builder()
                                .disabled(true)
                                .build())
                        .build();
        Fabric.with(context, crashlytics);
    }

    public static boolean hasLocation(double latitude, double longitude) {
        return latitude != 0 && longitude != 0;
    }

    public static void crash(String message) {
        throw new RuntimeException(message);
    }

    public static boolean connected(@NonNull Context context) {
        boolean connected = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isConnected();
        }
        return connected;
    }

    public static boolean hasData(@NonNull List<String> list) {
        if (list.isEmpty()) return false;
        final int length = list.size();
        for (int i = 0; i < length; i++) {
            final String s = list.get(i).trim();
            if (!s.isEmpty()) {
                return true;
            }
        }
        return false;
    }
}