package com.example.tourguidemegaphone.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.example.tourguidemegaphone.databases.RateReaderContract.RatesEntry;

import com.example.tourguidemegaphone.CurrencyConverterActivity;

public class CurrencyRatesDAO {
    private CurrencyRatesDbHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public CurrencyRatesDAO(Context c) {
        context = c;
    }

    public CurrencyRatesDAO open() throws SQLException {
        dbHelper = new CurrencyRatesDbHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String currencyFrom, String currencyTo, Double rate) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(CurrencyRatesDbHelper.CURRENCY_FROM, currencyFrom);
        contentValue.put(CurrencyRatesDbHelper.CURRENCY_TO, currencyTo);
        contentValue.put(CurrencyRatesDbHelper.RATE, rate);
        database.insert(CurrencyRatesDbHelper.TABLE_NAME, null, contentValue);
    }

/*    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.SUBJECT, DatabaseHelper.DESC };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }*/

/*    public int update(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SUBJECT, name);
        contentValues.put(DatabaseHelper.DESC, desc);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }*/

    public void delete(long _id) {
        database.delete(CurrencyRatesDbHelper.TABLE_NAME, CurrencyRatesDbHelper._ID + "=" + _id, null);
    }

}
