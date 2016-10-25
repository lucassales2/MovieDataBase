package com.moviedatabase.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;

import com.moviedatabase.MovieApplication;
import com.moviedatabase.R;
import com.moviedatabase.data.MovieContract;
import com.moviedatabase.networking.movies.MovieApiService;
import com.moviedatabase.networking.movies.MovieDetailApiService;
import com.moviedatabase.networking.movies.dto.MovieDetailsDto;
import com.moviedatabase.networking.movies.dto.MovieDto;
import com.moviedatabase.networking.movies.dto.ReviewDto;
import com.moviedatabase.networking.movies.dto.VideoDto;
import com.moviedatabase.networking.movies.responses.MoviesResponse;
import com.moviedatabase.networking.movies.responses.ReviewResponse;
import com.moviedatabase.networking.movies.responses.VideoResponse;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;


public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final String MOVIE_ID = "movieId";
    public final String LOG_TAG = MovieSyncAdapter.class.getSimpleName();
    @Inject
    MovieDetailApiService movieDetailApiService;
    @Inject
    MovieApiService movieApiService;
    @Inject
    SharedPreferences sharedPreferences;

    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        MovieApplication.getInstance().getComponent().inject(this);
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    public static void syncImmediatelyWithMovieId(Context context, long movieId) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putLong(MOVIE_ID, movieId);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        MovieSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        if (extras.containsKey(MOVIE_ID)) {
            syncWithMovieId(extras.getLong(MOVIE_ID));
        } else {
            getMovies()
                    .subscribe(new Subscriber<ContentValues[]>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(ContentValues[] values) {
                            getContext().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, values);
                        }
                    });
        }
    }

    private void syncWithMovieId(final long movieId) {
        getMovieDetails(movieId)
                .subscribe(new Subscriber<ContentValues>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ContentValues contentValues) {
                        getContext().getContentResolver().update(
                                MovieContract.MovieEntry.CONTENT_URI,
                                contentValues,
                                MovieContract.MovieEntry._ID + " =?",
                                new String[]{String.valueOf(movieId)});
                    }
                });

        getVideosFromMovieId(movieId)
                .subscribe(new Subscriber<ContentValues[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ContentValues[] values) {
                        getContext().getContentResolver().bulkInsert(MovieContract.VideoEntry.CONTENT_URI, values);
                    }
                });

        getReviewsFromMovieId(movieId)
                .subscribe(new Subscriber<ContentValues[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ContentValues[] values) {
                        getContext().getContentResolver().bulkInsert(MovieContract.ReviewEntry.CONTENT_URI, values);
                    }
                });
    }

    private Observable<ContentValues> getMovieDetails(long movieId) {
        return movieDetailApiService.getMovieDetails(movieId)
                .map(new Func1<MovieDetailsDto, ContentValues>() {
                    @Override
                    public ContentValues call(MovieDetailsDto movieDetailsDto) {
                        ContentValues values = new ContentValues();
                        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieDetailsDto.getReleaseDateInMillis());
                        values.put(MovieContract.MovieEntry.COLUMN_RATING, movieDetailsDto.getVoteAverage());
                        values.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movieDetailsDto.getPopularity());
                        values.put(MovieContract.MovieEntry.COLUMN_RUNTIME, movieDetailsDto.getRuntime());
                        return values;
                    }
                });
    }

    private Observable<ContentValues[]> getMovies() {
        Observable<MoviesResponse> moviesResponseObservable;
        String title = sharedPreferences.getString(getContext().getString(R.string.movie_option), getContext().getString(R.string.top_rated));
        if (title.equals(getContext().getString(R.string.popular))) {
            moviesResponseObservable = movieApiService.getPopularMovies();
        } else if (title.equals(getContext().getString(R.string.upcoming))) {
            moviesResponseObservable = movieApiService.getUpcomingMovies();
        } else if (title.equals(getContext().getString(R.string.now_playing))) {
            moviesResponseObservable = movieApiService.getNowPlayingMovies();
        } else {
            moviesResponseObservable = movieApiService.getTopRatedMovies();
        }
        return moviesResponseObservable
                .map(new Func1<MoviesResponse, ContentValues[]>() {
                    @Override
                    public ContentValues[] call(MoviesResponse moviesResponse) {
                        ContentValues[] contentValues = new ContentValues[moviesResponse.results.size()];
                        List<MovieDto> results = moviesResponse.results;
                        for (int i = 0; i < results.size(); i++) {
                            MovieDto movieDto = results.get(i);
                            ContentValues values = new ContentValues();
                            values.put(MovieContract.MovieEntry._ID, movieDto.getId());
                            values.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movieDto.getOriginalLanguage());
                            values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieDto.getReleaseDateInMillis());
                            values.put(MovieContract.MovieEntry.COLUMN_RATING, movieDto.getVoteAverage());
                            values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movieDto.getPosterPath());
                            values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movieDto.getOverview());
                            values.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movieDto.getOriginalTitle());
                            values.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movieDto.getPopularity());
                            values.put(MovieContract.MovieEntry.COLUMN_TITLE, movieDto.getTitle());
                            contentValues[i] = values;
                        }
                        return contentValues;
                    }
                });
    }

    private Observable<ContentValues[]> getReviewsFromMovieId(long movieId) {
        return movieDetailApiService.getReviewsFromMovieId(movieId)
                .map(new Func1<ReviewResponse, ContentValues[]>() {
                    @Override
                    public ContentValues[] call(ReviewResponse reviewResponse) {
                        ContentValues[] valuesList = new ContentValues[reviewResponse.results.size()];
                        List<ReviewDto> results = reviewResponse.results;
                        for (int i = 0; i < results.size(); i++) {
                            ReviewDto reviewDto = results.get(i);
                            ContentValues values = new ContentValues();
                            values.put(MovieContract.ReviewEntry._ID, reviewDto.getId());
                            values.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, reviewResponse.id);
                            values.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, reviewDto.getAuthor());
                            values.put(MovieContract.ReviewEntry.COLUMN_CONTENT, reviewDto.getContent());
                            values.put(MovieContract.ReviewEntry.COLUMN_URL, reviewDto.getUrl());
                            valuesList[i] = values;
                        }
                        return valuesList;
                    }
                });
    }

    private Observable<ContentValues[]> getVideosFromMovieId(long movieId) {
        return movieDetailApiService.getVideosFromMovieId(movieId)
                .map(new Func1<VideoResponse, ContentValues[]>() {
                    @Override
                    public ContentValues[] call(VideoResponse videoResponse) {
                        ContentValues[] valuesList = new ContentValues[videoResponse.results.size()];
                        List<VideoDto> results = videoResponse.results;
                        for (int i = 0; i < results.size(); i++) {
                            VideoDto videoDto = results.get(i);
                            ContentValues values = new ContentValues();
                            values.put(MovieContract.VideoEntry._ID, videoDto.getId());
                            values.put(MovieContract.VideoEntry.COLUMN_KEY, videoDto.getKey());
                            values.put(MovieContract.VideoEntry.COLUMN_MOVIE_ID, videoResponse.id);
                            values.put(MovieContract.VideoEntry.COLUMN_NAME, videoDto.getName());
                            values.put(MovieContract.VideoEntry.COLUMN_SITE, videoDto.getSite());
                            valuesList[i] = values;
                        }
                        return valuesList;
                    }
                });
    }
}