package com.example.tourguidemegaphone.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class LoginDbHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "Login.db";
    private static final int DB_VERSION = 5;
    private Context context;

    public LoginDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + LoginContract.LoginEntry.TABLE_NAME + " (" +
                        LoginContract.LoginEntry._ID + " INTEGER PRIMARY KEY," +
                        LoginContract.LoginEntry.COLUMN_NAME_EMAIL + " TEXT," +
                        LoginContract.LoginEntry.COLUMN_NAME_TOKEN + " TEXT)";

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LoginContract.LoginEntry.TABLE_NAME);
        onCreate(db);
    }

}
