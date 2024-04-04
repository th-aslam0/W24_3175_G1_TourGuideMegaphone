package com.example.tourguidemegaphone.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tourguidemegaphone.databases.RateReaderContract.*;

public class CurrencyRatesDbHelper extends SQLiteOpenHelper {
    private Context context;

    // Database Information
    static String DB_NAME = "RatesReader.db";

    // database version
    static final int DB_VERSION = 5;

    // Creating table query

    private static final String CREATE_CURRENCY_RATES_TABLE = "CREATE TABLE IF NOT EXISTS CurrencyRates" +
            "(id INTEGER PRIMARY KEY, " +
            "result TEXT, " +
            "provider TEXT, " +
            "documentation TEXT, " +
            "terms_of_use TEXT, " +
            "time_last_update_unix INTEGER, " +
            "time_last_update_utc TEXT, " +
            "time_next_update_unix INTEGER, " +
            "time_next_update_utc TEXT, " +
            "time_eol_unix INTEGER, " +
            "base_code TEXT)";
    private static final String CREATE_RATES_TABLE = "CREATE TABLE IF NOT EXISTS Rates" +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "currency TEXT, " +
            "rate REAL, " +
            "currencyRatesId INTEGER, " +
            "FOREIGN KEY(currencyRatesId) REFERENCES CurrencyRates(id))";


    public CurrencyRatesDbHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CURRENCY_RATES_TABLE);
        db.execSQL(CREATE_RATES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "CurrencyRates");
        db.execSQL("DROP TABLE IF EXISTS " + "Rates");
        onCreate(db);
    }
}
