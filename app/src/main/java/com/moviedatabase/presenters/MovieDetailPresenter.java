package com.moviedatabase.presenters;

import com.moviedatabase.MovieApplication;
import com.moviedatabase.networking.movies.MovieDetailApiService;
import com.moviedatabase.networking.movies.dto.MovieDetailsDto;
import com.moviedatabase.networking.movies.responses.ReviewResponse;
import com.moviedatabase.networking.movies.responses.VideoResponse;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lucas on 11/10/16.
 */

public class MovieDetailPresenter {
    private final int movieId;
    private final WeakReference<MovieDetailPresenterListener> listener;
    @Inject
    MovieDetailApiService movieApiService;

    public MovieDetailPresenter(int movieId, MovieDetailPresenterListener listener) {
        this.movieId = movieId;
        this.listener = new WeakReference<>(listener);
        MovieApplication.getInstance().getComponent().inject(this);
    }

    private void fetchMovieDetails() {
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

    public void init() {
        fetchMovieDetails();
        fetchMovieVideos();
        fetchMovieReviews();
    }

    private void fetchMovieReviews() {
        movieApiService.getReviewsFromMovieId(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ReviewResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ReviewResponse reviewResponse) {
                        listener.get().onReviewsLoaded(reviewResponse.results);
                    }
                });
    }

    private void fetchMovieVideos() {
        movieApiService.getVideosFromMovieId(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VideoResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(VideoResponse videoResponse) {
                        listener.get().onVideosLoaded(videoResponse.results);
                    }
                });
    }
}
