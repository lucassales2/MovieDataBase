package com.moviedatabase.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by lucas on 19/10/16.
 */

public class MovieContract {

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_VIDEO = "video";
    public static final String PATH_REVIEW = "review";
    public static String CONTENT_AUTHORITY = "com.moviedatabase";
    public static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "originalLanguage";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_FAVORITED = "favorited";


        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class VideoEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_VIDEO).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;

        public static final String TABLE_NAME = "video";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SITE = "site";


        public static Uri buildVideoeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ReviewEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_REVIEW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        public static final String TABLE_NAME = "review";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";


        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildReviewUriFromMovieId(long movieId) {
            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_MOVIE_ID, String.valueOf(movieId)).build();
        }

        public static long getMovieIdFromUri(Uri uri) {
            String queryParameter = uri.getQueryParameter(COLUMN_MOVIE_ID);
            if (queryParameter != null && !queryParameter.isEmpty())
                return Long.valueOf(queryParameter);
            else
                return 0;
        }
    }

}
