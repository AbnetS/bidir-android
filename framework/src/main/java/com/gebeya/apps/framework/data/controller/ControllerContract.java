package com.gebeya.apps.framework.data.controller;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gebeya.apps.framework.data.column.Column;
import com.gebeya.apps.framework.data.model.BaseModel;
import com.gebeya.apps.framework.data.util.ValuesWrapper;

public interface ControllerContract {

    void setTable(String table);

    String getTable();

    boolean insert(ValuesWrapper wrapper);

    boolean update(ValuesWrapper wrapper, String where, String arg);

    boolean update(ValuesWrapper wrapper, String[] where, String[] args);

    boolean delete();

    boolean delete(Column where, BaseModel arg);

    boolean delete(String where, String arg);

    boolean delete(String[] where, String[] args);

    Cursor find();

    Cursor findById(String id);

    Cursor find(String where, String arg);

    Cursor find(String[] where, String[] args);

    void close(Cursor cursor);

    boolean hasData(Cursor cursor);

    SQLiteDatabase open();

    byte[] getBlob(Column column, Cursor cursor);

    long getLong(Column column, Cursor cursor);

    double getDouble(Column column, Cursor cursor);

    float getFloat(Column column, Cursor cursor);

    short getShort(Column column, Cursor cursor);

    int getInt(Column column, Cursor cursor);

    String getString(Column column, Cursor cursor);
}
