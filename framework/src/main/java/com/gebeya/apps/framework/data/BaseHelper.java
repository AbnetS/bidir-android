package com.gebeya.apps.framework.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Locale;

public abstract class BaseHelper extends SQLiteOpenHelper {

    public BaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        d("< --------- onCreate(SQLiteDatabase) --------- >");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        e(String.format(Locale.getDefault(), "< --------- onUpgrade(SQLiteDatabase, %d, %d) --------- >", oldVersion, newVersion));
    }

    protected abstract void d(String message);

    protected abstract void e(String message);

}
