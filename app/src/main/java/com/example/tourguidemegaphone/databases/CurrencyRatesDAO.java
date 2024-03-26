package com.example.tourguidemegaphone.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.tourguidemegaphone.model.CurrencyRates;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CurrencyRatesDAO {
    private CurrencyRatesDbHelper dbHelper;

    private Context context;
    private Gson gson;

    private SQLiteDatabase database;

    public CurrencyRatesDAO(Context c) {
        context = c;
    }

    public CurrencyRatesDAO open() throws SQLException {
        dbHelper = new CurrencyRatesDbHelper(context);
        database = dbHelper.getWritableDatabase();
        this.gson = new Gson();

        return this;
    }

    public void storeCurrencyRates(CurrencyRates currencyRates) {
        // Parse the JSON
        //CurrencyRates currencyRates = gson.fromJson(json, CurrencyRates.class);

        // Get a writable database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Insert the CurrencyRates into the database
        ContentValues values = new ContentValues();
        values.put("result", currencyRates.getResult());
        values.put("provider", currencyRates.getProvider());
        values.put("documentation", currencyRates.getDocumentation());
        values.put("terms_of_use", currencyRates.getTerms_of_use());
        values.put("time_last_update_unix", currencyRates.getTime_last_update_unix());
        values.put("time_last_update_utc", currencyRates.getTime_last_update_utc());
        values.put("time_next_update_unix", currencyRates.getTime_next_update_unix());
        values.put("time_next_update_utc", currencyRates.getTime_next_update_utc());
        values.put("time_eol_unix", currencyRates.getTime_eol_unix());
        values.put("base_code", currencyRates.getBase_code());
        long currencyRatesId = db.insert("CurrencyRates", null, values);

        // Insert the rates into the database
        for (Map.Entry<String, Double> entry : currencyRates.getRates().entrySet()) {
            ContentValues rateValues = new ContentValues();
            rateValues.put("currency", entry.getKey());
            rateValues.put("rate", entry.getValue());
            rateValues.put("currencyRatesId", currencyRatesId);
            db.insert("Rates", null, rateValues);
        }
    }

    public void close() {
        dbHelper.close();
    }

    public long getLastUpdate(String currency) {
        // Define a projection that specifies which columns from the database you will use after this query
        String[] projection = {"time_next_update_unix"};

        // Filter results WHERE "id" = '1'
        String selection = "base_code = ?";
        String[] selectionArgs = {currency};

        // Query the table
        Cursor cursor = database.query(
                "CurrencyRates",   // The table to query
                projection,          // The array of columns to return (pass null to get all)
                selection,           // The columns for the WHERE clause
                selectionArgs,       // The values for the WHERE clause
                null,                // Don't group the rows
                null,                // Don't filter by row groups
                null                 // Don't sort the rows
        );

        try {
            if (cursor.moveToFirst()) {
                // Get the value of the UNIXTIME column
                long lastUnixTime = cursor.getLong(cursor.getColumnIndexOrThrow("time_next_update_unix"));
                return lastUnixTime;
            } else {
                // If the cursor is empty, return a default value or throw an exception
                return -1;
            }
        } finally {
            cursor.close();
        }
    }
    private int getCurrencyId(String currency){

        // Define a projection that specifies which columns from the database you will use after this query
        String[] projection = {"id"};

        // Filter results WHERE "id" = '1'
        String selection = "base_code = ?";
        String[] selectionArgs = {currency};

        // Query the table
        Cursor cursor = database.query(
                "CurrencyRates",   // The table to query
                projection,          // The array of columns to return (pass null to get all)
                selection,           // The columns for the WHERE clause
                selectionArgs,       // The values for the WHERE clause
                null,                // Don't group the rows
                null,                // Don't filter by row groups
                null                 // Don't sort the rows
        );

        try {
            if (cursor.moveToFirst()) {
                // Get the value of the id column
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                return id;
            } else {
                // If the cursor is empty, return a default value or throw an exception
                return -1;
            }
        } finally {
            cursor.close();
        }


    }

    public Map<String, Double> getCurrencyRates(String currency) {
        int idCurrency = getCurrencyId(currency);

        Map<String, Double> currencyRates = new HashMap<>();

        // Define a projection that specifies which columns from the database you will use after this query
        String[] projection = {"currency", "rate"};

        // Filter results WHERE "COLUMN_NAME_CURRENCY_FROM" = 'currency'
        String selection = "currencyRatesId" + " = ?";
        String[] selectionArgs = {String.valueOf(idCurrency)};

        // Query the table
        Cursor cursor = database.query(
                "Rates",   // The table to query
                projection,          // The array of columns to return (pass null to get all)
                selection,           // The columns for the WHERE clause
                selectionArgs,       // The values for the WHERE clause
                null,                // Don't group the rows
                null,                // Don't filter by row groups
                null                 // Don't sort the rows
        );

        try {
            while (cursor.moveToNext()) {
                String currencyTo = cursor.getString(cursor.getColumnIndexOrThrow("currency"));
                double rate = cursor.getDouble(cursor.getColumnIndexOrThrow("rate"));
                currencyRates.put(currencyTo, rate);
            }
        } finally {
            cursor.close();
        }

        return currencyRates;
    }
}
