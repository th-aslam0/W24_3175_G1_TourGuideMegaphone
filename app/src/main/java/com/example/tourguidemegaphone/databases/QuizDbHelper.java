package com.example.tourguidemegaphone.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quiz.db";
    private static final int DATABASE_VERSION = 2;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_QUIZ_TABLE = "CREATE TABLE " + QuizContract.QuizEntry.TABLE_NAME + "("
                + QuizContract.QuizEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + QuizContract.QuizEntry.COLUMN_CITY + " TEXT NOT NULL, "
                + QuizContract.QuizEntry.COLUMN_QUESTION + " TEXT NOT NULL, "
                + QuizContract.QuizEntry.COLUMN_OPTION1 + " TEXT NOT NULL, "
                + QuizContract.QuizEntry.COLUMN_OPTION2 + " TEXT NOT NULL, "
                + QuizContract.QuizEntry.COLUMN_OPTION3 + " TEXT NOT NULL, "
                + QuizContract.QuizEntry.COLUMN_OPTION4 + " TEXT NOT NULL, "
                + QuizContract.QuizEntry.COLUMN_ANSWER + " INTEGER NOT NULL)";
        db.execSQL(CREATE_QUIZ_TABLE);

        //populateDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuizContract.QuizEntry.TABLE_NAME);
        onCreate(db);
    }

    public void emptyAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Get a list of all tables in the database
            Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            List<String> tables = new ArrayList<>();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    tables.add(cursor.getString(0));
                }
                cursor.close();
            }

            // Iterate over each table and delete all rows
            for (String table : tables) {
                if (!table.equals("sqlite_sequence")) { // Skip system table
                    db.delete(table, null, null);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }


}
