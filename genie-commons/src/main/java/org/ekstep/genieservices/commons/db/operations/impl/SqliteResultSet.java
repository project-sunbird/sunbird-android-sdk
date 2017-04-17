package org.ekstep.genieservices.commons.db.operations.impl;

import android.database.Cursor;

import org.ekstep.genieservices.commons.db.operations.ResultSet;

/**
 * Created by swayangjit on 17/4/17.
 */

public class SqliteResultSet implements ResultSet {

    private Cursor cursor;

    public SqliteResultSet(Cursor cursor){
        this.cursor=cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public int getPosition() {
        return cursor.getPosition();
    }

    @Override
    public boolean moveToFirst() {
        return cursor.moveToFirst();
    }


    @Override
    public int getColumnIndex(String columnName) {
        return cursor.getColumnIndex(columnName);
    }

    @Override
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
        return cursor.getColumnIndexOrThrow(columnName);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return cursor.getColumnName(columnIndex);
    }

    @Override
    public String[] getColumnNames() {
        return cursor.getColumnNames();
    }

    @Override
    public int getColumnCount() {
        return cursor.getColumnCount();
    }


    @Override
    public String getString(int columnIndex) {
        return cursor.getString(columnIndex);
    }


    @Override
    public int getInt(int columnIndex) {
        return cursor.getInt(columnIndex);
    }

    @Override
    public long getLong(int columnIndex) {
        return cursor.getLong(columnIndex);
    }

    @Override
    public double getDouble(int columnIndex) {
        return cursor.getDouble(columnIndex);
    }
}
