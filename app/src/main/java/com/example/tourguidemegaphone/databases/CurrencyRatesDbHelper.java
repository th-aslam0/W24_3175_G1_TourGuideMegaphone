package com.example.tourguidemegaphone.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tourguidemegaphone.databases.RateReaderContract.*;

public class CurrencyRatesDbHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = RatesEntry.TABLE_NAME;

    // Table columns
    public static final String _ID = "_id";
    public static final String CURRENCY_FROM = RatesEntry.COLUMN_NAME_CURRENCY_FROM;
    public static final String CURRENCY_TO = RatesEntry.COLUMN_NAME_CURRENCY_TO;
    public static final String RATE = RatesEntry.COLUMN_NAME_RATE;

    // Database Information
    static final String DB_NAME = "RatesReader.db";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE =
            "CREATE TABLE " + RateReaderContract.RatesEntry.TABLE_NAME + " (" +
                    RatesEntry._ID + " INTEGER PRIMARY KEY," +
                    RatesEntry.COLUMN_NAME_CURRENCY_FROM + " TEXT," +
                    RatesEntry.COLUMN_NAME_CURRENCY_TO + " TEXT," +
                    RatesEntry.COLUMN_NAME_RATE + "  REAL)";

    public CurrencyRatesDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // If you change the database schema, you must increment the database version.
    /*public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RatesReader.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + RateReaderContract.RatesEntry.TABLE_NAME + " (" +
                    RateReaderContract.RatesEntry._ID + " INTEGER PRIMARY KEY," +
                    RateReaderContract.RatesEntry.COLUMN_NAME_CURRENCY_FROM + " TEXT," +
                    RateReaderContract.RatesEntry.COLUMN_NAME_CURRENCY_TO + " TEXT," +
                    RateReaderContract.RatesEntry.COLUMN_NAME_RATE + "  REAL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + RateReaderContract.RatesEntry.TABLE_NAME;

    public CurrencyRatesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }*/



}
