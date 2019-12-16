package com.gebeya.apps.framework.data.schema;

import android.database.sqlite.SQLiteDatabase;

public final class Schemas {

    public static BaseSchema[] SCHEMAS;

    private static void checkSchemas() {
        if (SCHEMAS == null || SCHEMAS.length == 0)
            throw new RuntimeException("No Schemas provided in the SCHEMAS array");
    }

    public static void create(SQLiteDatabase db) {
        checkSchemas();
        try {
            for (BaseSchema schema : SCHEMAS) {
                String create = schema.CREATE;
                db.execSQL(create);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void delete(SQLiteDatabase db) {
        checkSchemas();
        try {
            for (BaseSchema schema : SCHEMAS) {
                String delete = schema.DELETE;
                db.execSQL(delete);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void upgrade(SQLiteDatabase db) {
        delete(db);
        create(db);
    }
}
