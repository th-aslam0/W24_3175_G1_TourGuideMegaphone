package com.example.tourguidemegaphone.databases;

import android.provider.BaseColumns;

public final class LoginContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private LoginContract() {}

    /* Inner class that defines the table contents */
    public static class LoginEntry implements BaseColumns {
        public static final String TABLE_NAME = "login";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_TOKEN = "token";
    }
}