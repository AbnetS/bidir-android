package com.gebeya.apps.framework.data;

import android.database.sqlite.SQLiteDatabase;

public class Database {

    private static BaseHelper helper;
    private static SQLiteDatabase db;

    public static synchronized SQLiteDatabase open() {
        checkHelper();
        if (db == null) {
            db = helper.getWritableDatabase();
        }
        return db;
    }

    public static void setHelper(BaseHelper newHelper) {
        if (helper == null) {
            helper = newHelper;
        }
    }

    private static void checkHelper() {
        if (helper == null) {
            throw new RuntimeException("BaseHelper has not been set");
        }
    }
}
