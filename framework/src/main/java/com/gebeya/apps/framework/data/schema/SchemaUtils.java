package com.gebeya.apps.framework.data.schema;

import android.support.annotation.NonNull;

import com.gebeya.apps.framework.data.column.Column;

import java.util.Locale;

public class SchemaUtils {

    public static String buildCreate(String table, @NonNull Column... columns) {
        if (columns.length == 0) {
            throw new RuntimeException("No Columns provided");
        }
        int count = columns.length;
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ").append(table);
        builder.append(" (\n");
        for (int i = 0; i < count; i++) {
            Column column = columns[i];
            builder.append("\t").append(column.toSql());
            builder.append(i == count - 1 ? "" : ",");
            builder.append("\n");
        }
        builder.append(")");
        return builder.toString();
    }

    public static String buildDelete(String table) {
        return String.format(Locale.getDefault(), "DROP TABLE IF EXISTS %s", table);
    }
}