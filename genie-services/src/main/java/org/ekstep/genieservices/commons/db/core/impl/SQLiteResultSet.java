package org.ekstep.genieservices.commons.db.core.impl;

import android.database.Cursor;

import org.ekstep.genieservices.commons.db.core.IResultSet;

/**
 * Created on 17/4/17.
 *
 * @author swayangjit
 */
public class SQLiteResultSet implements IResultSet {

    private Cursor cursor;

    public SQLiteResultSet(Cursor cursor) {
        this.cursor = cursor;
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

    @Override
    public byte[] getBlob(int columnIndex) {
        return cursor.getBlob(columnIndex);
    }
}
