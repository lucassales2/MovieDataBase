package com.moviedatabase.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.moviedatabase.data.MovieContract.MovieEntry;
import com.moviedatabase.data.MovieContract.ReviewEntry;
import com.moviedatabase.data.MovieContract.VideoEntry;

/**
 * Created by lucas on 19/10/16.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createMovieTable(db);
        createVideoTable(db);
        createReviewTable(db);
    }

    private void createMovieTable(SQLiteDatabase db) {
        String sqlCreateMovieTable = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RUNTIME + " TEXT, " +
                MovieEntry.COLUMN_FAVORITED + " INTEGER DEFAULT 0, " +
                MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                MovieEntry.COLUMN_RATING + " REAL NOT NULL)";
        db.execSQL(sqlCreateMovieTable);
    }

    private void createVideoTable(SQLiteDatabase db) {
        String sqlCreateVideoTable = "CREATE TABLE " + VideoEntry.TABLE_NAME + " (" +
                VideoEntry._ID + " TEXT PRIMARY KEY, " +
                VideoEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                VideoEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                VideoEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                VideoEntry.COLUMN_SITE + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + VideoEntry.COLUMN_MOVIE_ID + ") REFERENCES " + MovieEntry.TABLE_NAME + "(" + MovieEntry._ID + "))";

        db.execSQL(sqlCreateVideoTable);
    }

    private void createReviewTable(SQLiteDatabase db) {
        String sqlCreateReviewTable = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
                ReviewEntry._ID + " TEXT PRIMARY KEY, " +
                ReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                ReviewEntry.COLUMN_URL + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + ReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES " + MovieEntry.TABLE_NAME + "(" + MovieEntry._ID + "))";
        db.execSQL(sqlCreateReviewTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
