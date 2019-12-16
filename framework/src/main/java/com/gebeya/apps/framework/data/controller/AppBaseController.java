package com.gebeya.apps.framework.data.controller;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gebeya.apps.framework.data.Database;
import com.gebeya.apps.framework.data.column.Column;
import com.gebeya.apps.framework.data.model.BaseModel;
import com.gebeya.apps.framework.data.schema.BaseSchema;
import com.gebeya.apps.framework.data.util.CursorUtils;
import com.gebeya.apps.framework.data.util.ValuesWrapper;
import com.gebeya.apps.framework.util.Loggable;

public abstract class AppBaseController implements ControllerContract, Loggable {

    private String table;
    private SQLiteDatabase db;

    public AppBaseController() {
        db = open();
    }

    @Override
    public void setTable(String table) {
        this.table = table;
    }

    @Override
    public String getTable() {
        return table;
    }

    @Override
    public boolean insert(ValuesWrapper wrapper) {
        long rowId = db.insert(table, null, wrapper.getValues());
        return rowId != -1;
    }

    @Override
    public boolean update(ValuesWrapper wrapper, String where, String arg) {
        int updated = db.update(table, wrapper.getValues(), where, new String[]{arg});
        return updated != 0;
    }

    @Override
    public boolean update(ValuesWrapper wrapper, String[] where, String[] args) {
        int updated = db.update(table, wrapper.getValues(), createWhere(where), args);
        return updated != 0;
    }

    @Override
    public boolean delete() {
        db.delete(table, "1", null);
        return true;
    }

    @Override
    public boolean delete(Column where, BaseModel arg) {
        return delete(where.name, arg.id);
    }

    @Override
    public boolean delete(String where, String arg) {
        return delete(new String[]{where}, new String[]{arg});
    }

    @Override
    public boolean delete(String[] where, String[] args) {
        int deleted = db.delete(table, createWhere(where), args);
        return deleted != 0;
    }

    @Override
    public Cursor find() {
        String sql = "SELECT * FROM " + table;
        return db.rawQuery(sql, null);
    }

    @Override
    public Cursor findById(String id) {
        return find(BaseSchema.COL_ID.name, id);
    }

    @Override
    public Cursor find(String where, String arg) {
        String sql = "SELECT * FROM " + table + " " + createWhere(where);
        return db.rawQuery(sql, new String[]{arg});
    }

    @Override
    public Cursor find(String[] where, String[] args) {
        String sql = "SELECT * FROM " + table + " " + createWhere(where);
        return db.rawQuery(sql, args);
    }

    @Override
    @SuppressWarnings("all")
    public void close(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor = null;
    }

    private String createWhere(String... values) {
        String where = "";
        int length = values.length;
        for (int i = 0; i < length; i++) {
            where += (values[i] + " = ?");
            where += i == length - 1 ? "" : " AND ";
        }
        return where;
    }

    @Override
    public boolean hasData(Cursor cursor) {
        return cursor != null && cursor.moveToFirst();
    }

    @Override
    public SQLiteDatabase open() {
        return Database.open();
    }

    @Override
    public byte[] getBlob(Column column, Cursor cursor) {
        return CursorUtils.getBlob(column, cursor);
    }

    @Override
    public long getLong(Column column, Cursor cursor) {
        return CursorUtils.getLong(column, cursor);
    }

    @Override
    public double getDouble(Column column, Cursor cursor) {
        return CursorUtils.getDouble(column, cursor);
    }

    @Override
    public float getFloat(Column column, Cursor cursor) {
        return CursorUtils.getFloat(column, cursor);
    }

    @Override
    public short getShort(Column column, Cursor cursor) {
        return CursorUtils.getShort(column, cursor);
    }

    @Override
    public int getInt(Column column, Cursor cursor) {
        return CursorUtils.getInt(column, cursor);
    }

    @Override
    public String getString(Column column, Cursor cursor) {
        return CursorUtils.getString(column, cursor);
    }
}
