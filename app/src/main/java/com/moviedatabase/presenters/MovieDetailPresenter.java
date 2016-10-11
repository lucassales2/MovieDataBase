package com.moviedatabase.presenters;

import android.content.Context;

import com.moviedatabase.MovieApplication;
import com.moviedatabase.networking.movies.MovieApiService;
import com.moviedatabase.networking.movies.dto.MovieDetailsDto;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lucas on 11/10/16.
 */

public class MovieDetailPresenter {
    private final Context mContext;
    private final int movieId;
    private final WeakReference<MovieDetailPresenterListener> listener;
    @Inject
    MovieApiService movieApiService;

    public MovieDetailPresenter(Context context, int movieId, MovieDetailPresenterListener listener) {
        this.mContext = context;
        this.movieId = movieId;
        this.listener = new WeakReference<>(listener);
        MovieApplication.getInstance().getComponent().inject(this);
    }

    public void fetchMovieDetails() {
        movieApiService.getMovieDetails(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieDetailsDto>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MovieDetailsDto movieDetailsDto) {
                        listener.get().onMovieDetailsLoaded(movieDetailsDto);
                    }
                });
    }
}
