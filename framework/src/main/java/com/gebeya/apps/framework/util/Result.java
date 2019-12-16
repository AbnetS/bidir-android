package com.gebeya.apps.framework.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public final class Result {

    public static final String STATUS_OK = "OK";
    public static final String STATUS_ERROR = "ERROR";

    public String status;
    public String message;
    public String extra;

    private Result() {
        status = "";
        message = "";
        extra = "";
    }

    public boolean isOk() {
        return status.equals(STATUS_OK);
    }

    public static String createMessage(@NonNull List<String> messages) {
        StringBuilder builder = new StringBuilder();
        for (String message : messages) {
            builder.append(message).append("\n");
        }
        return builder.toString();
    }

    public static Result createOk() {
        return createOk("");
    }

    @SuppressWarnings("all")
    public static Result createOk(@NonNull String message) {
        return create(STATUS_OK, message);
    }

    public static Result createError(@NonNull String message) {
        return createError(message, "");
    }

    public static Result createError(@NonNull String message, @Nullable String extra) {
        return create(STATUS_ERROR, message, extra);
    }

    public static Result create(@NonNull String status, @NonNull String message) {
        return create(status, message, "");
    }

    public static Result create(@NonNull String status, @NonNull String message, @Nullable String extra) {
        Result result = new Result();
        result.status = status;
        result.message = message;
        result.extra = extra;
        return result;
    }

    @Override
    public String toString() {
        if (isOk()) {
            return STATUS_OK;
        } else {
            return extra.isEmpty() ? message : String.valueOf(extra);
        }
    }
}