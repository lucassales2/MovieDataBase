package com.moviedatabase.presenters;

import android.content.Context;
import android.content.SharedPreferences;

import com.moviedatabase.MovieApplication;
import com.moviedatabase.R;
import com.moviedatabase.networking.movies.MovieApiService;
import com.moviedatabase.networking.movies.responses.MoviesResponse;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lucas on 11/10/16.
 */

public class MovieListPresenter {

    private final Context mContext;
    @Inject
    MovieApiService movieApiService;
    @Inject
    SharedPreferences sharedPreferences;
    private WeakReference<MovieListPresenterListener> listener;

    public MovieListPresenter(Context context, MovieListPresenterListener listener) {
        this.mContext = context;
        this.listener = new WeakReference<>(listener);
        MovieApplication.getInstance().getComponent().inject(this);
    }


    public void fetchMovies(String title) {
        Observable<MoviesResponse> moviesResponseObservable;
        listener.get().onTitleChanged(title);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(mContext.getString(R.string.movie_option), title).apply();
        if (title.equals(mContext.getString(R.string.popular))) {
            moviesResponseObservable = movieApiService.getPopularMovies();
        } else if (title.equals(mContext.getString(R.string.upcoming))) {
            moviesResponseObservable = movieApiService.getUpcomingMovies();
        } else if (title.equals(mContext.getString(R.string.now_playing))) {
            moviesResponseObservable = movieApiService.getNowPlayingMovies();
        } else {
            moviesResponseObservable = movieApiService.getTopRatedMovies();
        }
        moviesResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MoviesResponse>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MoviesResponse moviesResponse) {
                        listener.get().onMoviesLoaded(moviesResponse.results);
                    }
                });
    }
}
