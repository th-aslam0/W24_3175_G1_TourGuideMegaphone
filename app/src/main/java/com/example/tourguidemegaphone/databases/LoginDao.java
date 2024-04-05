package com.example.tourguidemegaphone.databases;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LoginDao {
    private LoginDbHelper dbHelper;

    public LoginDao(Context context) {
        dbHelper = new LoginDbHelper(context);
    }

    public void saveUserSession(String email, String token) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LoginContract.LoginEntry.COLUMN_NAME_EMAIL, email);
        values.put(LoginContract.LoginEntry.COLUMN_NAME_TOKEN, token);

        // Check if email already exists in the database
        String selection = LoginContract.LoginEntry.COLUMN_NAME_EMAIL + " = ?";
        String[] selectionArgs = { email };
        Cursor cursor = db.query(
                LoginContract.LoginEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            // Email already exists, update the record
            db.update(
                    LoginContract.LoginEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
            );
        } else {

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(LoginContract.LoginEntry.TABLE_NAME, null, values);
        }

    }

    public String getUserToken(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                LoginContract.LoginEntry.COLUMN_NAME_TOKEN
        };

        String selection = LoginContract.LoginEntry.COLUMN_NAME_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(
                LoginContract.LoginEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String token = null;
        if (cursor.moveToFirst()) {
            token = cursor.getString(
                    cursor.getColumnIndexOrThrow(LoginContract.LoginEntry.COLUMN_NAME_TOKEN));
        }
        cursor.close();

        return token;
    }

    public String getLastLoginEmail() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                LoginContract.LoginEntry.COLUMN_NAME_EMAIL
        };

        // Order by _id DESC to get the last ID
        String sortOrder = LoginContract.LoginEntry._ID + " DESC";

        Cursor cursor = db.query(
                LoginContract.LoginEntry.TABLE_NAME,   // The table to query
                projection,                            // The array of columns to return (pass null to get all)
                null,                                  // The columns for the WHERE clause
                null,                                  // The values for the WHERE clause
                null,                                  // don't group the rows
                null,                                  // don't filter by row groups
                sortOrder,                             // The sort order
                "1"                                    // Limit the output
        );

        String email = null;
        if (cursor.moveToFirst()) {
            email = cursor.getString(
                    cursor.getColumnIndexOrThrow(LoginContract.LoginEntry.COLUMN_NAME_EMAIL));
        }
        cursor.close();

        return email;
    }

}

