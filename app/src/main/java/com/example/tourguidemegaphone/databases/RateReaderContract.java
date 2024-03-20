package com.example.tourguidemegaphone.databases;

import android.provider.BaseColumns;

public final class RateReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private RateReaderContract() {}

    /* Inner class that defines the table contents */
    public static class RatesEntry implements BaseColumns {
        public static final String TABLE_NAME = "CURRENCY_RATES";
        public static final String COLUMN_NAME_CURRENCY_FROM = "currencyFrom";
        public static final String COLUMN_NAME_CURRENCY_TO = "currencyTo";
        public static final String COLUMN_NAME_RATE = "rate";


    }
}