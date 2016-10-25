package com.moviedatabase.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import java.util.Map;

/**
 * Created by lucas on 19/10/16.
 */

public class MovieProvider extends ContentProvider {
    static final int MOVIE = 100;
    static final int MOVIE_ITEM = 101;
    static final int VIDEO = 200;
    static final int REVIEW = 300;
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder sMovieWithVideoAndReviewQueryBuilder;

    static {
        sMovieWithVideoAndReviewQueryBuilder = new SQLiteQueryBuilder();

    }

    private MovieDbHelper mOpenHelper;

    /*
        Students: Here is where you need to create the UriMatcher. This UriMatcher will
        match each URI to the MOVIE, MOVIE_ITEM, WEATHER_WITH_LOCATION_AND_DATE,
        and REVIEW integer constants defined above.  You can test this by uncommenting the
        testUriMatcher test within TestUriMatcher.
     */
    static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_REVIEW, REVIEW);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_VIDEO, VIDEO);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE + "/*", MOVIE_ITEM);
        return matcher;
    }

    public static long insertIgnoringConflict(SQLiteDatabase db,
                                              String table,
                                              String idColumn,
                                              ContentValues values) {
        try {
            return db.insertOrThrow(table, null, values);
        } catch (SQLException e) {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ");
            sql.append(idColumn);
            sql.append(" FROM ");
            sql.append(table);
            sql.append(" WHERE ");

            Object[] bindArgs = new Object[values.size()];
            int i = 0;
            for (Map.Entry<String, Object> entry : values.valueSet()) {
                sql.append((i > 0) ? " AND " : "");
                sql.append(entry.getKey());
                sql.append(" = ?");
                bindArgs[i++] = entry.getValue();
            }

            SQLiteStatement stmt = db.compileStatement(sql.toString());
            for (i = 0; i < bindArgs.length; i++) {
                DatabaseUtils.bindObjectToProgram(stmt, i + 1, bindArgs[i]);
            }

            try {
                return stmt.simpleQueryForLong();
            } finally {
                stmt.close();
            }
        }
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ITEM: {
                long id = ContentUris.parseId(uri);
                SQLiteDatabase readableDatabase = mOpenHelper.getReadableDatabase();
                retCursor = readableDatabase.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry._ID + " =?",
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        sortOrder,
                        null);
                break;
            }
            case MOVIE: {
                SQLiteDatabase readableDatabase = mOpenHelper.getReadableDatabase();
                retCursor = readableDatabase.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder,
                        null);
                break;
            }

            case REVIEW: {
                SQLiteDatabase readableDatabase = mOpenHelper.getReadableDatabase();
                retCursor = readableDatabase.query(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder,
                        null);
                break;
            }
            case VIDEO: {
                SQLiteDatabase readableDatabase = mOpenHelper.getReadableDatabase();
                retCursor = readableDatabase.query(
                        MovieContract.VideoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder,
                        null);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ITEM:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case REVIEW:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            case VIDEO:
                return MovieContract.VideoEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE: {
                long _id = insertIgnoringConflict(db, MovieContract.MovieEntry.TABLE_NAME, BaseColumns._ID, values);
                if (_id > 0) {
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEW: {
                long _id = insertIgnoringConflict(db, MovieContract.ReviewEntry.TABLE_NAME, BaseColumns._ID, values);
                if (_id > 0)
                    returnUri = MovieContract.ReviewEntry.buildReviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case VIDEO: {
                long _id = insertIgnoringConflict(db, MovieContract.VideoEntry.TABLE_NAME, BaseColumns._ID, values);
                if (_id > 0)
                    returnUri = MovieContract.VideoEntry.buildVideoeUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REVIEW:
                rowsDeleted = db.delete(
                        MovieContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case VIDEO:
                rowsDeleted = db.delete(
                        MovieContract.VideoEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE:
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case REVIEW:
                rowsUpdated = db.update(MovieContract.ReviewEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case VIDEO:
                rowsUpdated = db.update(MovieContract.VideoEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
