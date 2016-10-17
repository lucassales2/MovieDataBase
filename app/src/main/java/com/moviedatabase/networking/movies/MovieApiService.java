package com.moviedatabase.networking.movies;

import com.moviedatabase.networking.movies.responses.MoviesResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

import static com.moviedatabase.networking.movies.Constants.CONTROLLER;
import static com.moviedatabase.networking.movies.Constants.NOW_PLAYING;
import static com.moviedatabase.networking.movies.Constants.POPULAR;
import static com.moviedatabase.networking.movies.Constants.TOP_RATED;
import static com.moviedatabase.networking.movies.Constants.UPCOMING;

/**
 * Created by lucas on 27/09/16.
 */

public interface MovieApiService {

    @GET(CONTROLLER + "/" + TOP_RATED)
    Observable<MoviesResponse> getTopRatedMovies();

    @GET(CONTROLLER + "/" + TOP_RATED)
    Observable<MoviesResponse> getTopRatedMovies(@Query("page") int page);

    //------------

    @GET(CONTROLLER + "/" + POPULAR)
    Observable<MoviesResponse> getPopularMovies();

    @GET(CONTROLLER + "/" + POPULAR)
    Observable<MoviesResponse> getPopularMovies(@Query("page") int page);

    //------------

    @GET(CONTROLLER + "/" + NOW_PLAYING)
    Observable<MoviesResponse> getNowPlayingMovies();

    @GET(CONTROLLER + "/" + NOW_PLAYING)
    Observable<MoviesResponse> getNowPlayingMovies(@Query("page") int page);

    //------------

    @GET(CONTROLLER + "/" + UPCOMING)
    Observable<MoviesResponse> getUpcomingMovies();

    @GET(CONTROLLER + "/" + UPCOMING)
    Observable<MoviesResponse> getUpcomingMovies(@Query("page") int page);

    //----------




}
