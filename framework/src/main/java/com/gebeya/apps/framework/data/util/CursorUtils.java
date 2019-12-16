package com.gebeya.apps.framework.data.util;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.gebeya.apps.framework.data.column.Column;

public class CursorUtils {

    public static byte[] getBlob(Column column, Cursor cursor) {
        return cursor.getBlob(getIndex(column, cursor));
    }

    public static long getLong(Column column, Cursor cursor) {
        return cursor.getLong(getIndex(column, cursor));
    }

    public static double getDouble(Column column, Cursor cursor) {
        return cursor.getDouble(getIndex(column, cursor));
    }

    public static float getFloat(Column column, Cursor cursor) {
        return cursor.getFloat(getIndex(column, cursor));
    }

    public static short getShort(Column column, Cursor cursor) {
        return cursor.getShort(getIndex(column, cursor));
    }

    public static int getInt(Column column, Cursor cursor) {
        return cursor.getInt(getIndex(column, cursor));
    }

    public static String getString(Column column, Cursor cursor) {
        return cursor.getString(getIndex(column, cursor));
    }

    private static int getIndex(@NonNull Column column, @NonNull Cursor cursor) {
        return cursor.getColumnIndexOrThrow(column.name);
    }
}
