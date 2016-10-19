package com.moviedatabase.sync;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

import com.moviedatabase.MovieApplication;
import com.moviedatabase.R;
import com.moviedatabase.data.MovieContract;
import com.moviedatabase.networking.movies.MovieApiService;
import com.moviedatabase.networking.movies.MovieDetailApiService;
import com.moviedatabase.networking.movies.dto.MovieDto;
import com.moviedatabase.networking.movies.responses.MoviesResponse;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lucas on 19/10/16.
 */

public class SyncMovie {
    @Inject
    MovieDetailApiService movieDetailApiService;
    @Inject
    MovieApiService movieApiService;
    @Inject
    SharedPreferences sharedPreferences;
    private Context mContext;

    public SyncMovie(Context context) {
        mContext = context;
        MovieApplication.getInstance().getComponent().inject(this);
    }

    private Context getContext() {
        return mContext;
    }

    public Observable<Integer> getMovies() {
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
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<MoviesResponse, Observable<MovieDto>>() {
                    @Override
                    public Observable<MovieDto> call(MoviesResponse moviesResponse) {
                        return Observable.from(moviesResponse.results);
                    }
                })
                .map(new Func1<MovieDto, ContentValues>() {
                    @Override
                    public ContentValues call(MovieDto movieDto) {
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
                        return values;
                    }
                })
                .toList()
                .map(new Func1<List<ContentValues>, Integer>() {
                    @Override
                    public Integer call(List<ContentValues> contentValues) {
                        return getContext().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, contentValues.toArray(new ContentValues[contentValues.size()]));
                    }
                });
    }
}
