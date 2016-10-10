package com.moviedatabase.networking.movies;

import com.moviedatabase.networking.movies.dto.MoviesResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by lucas on 27/09/16.
 */

public interface MovieApiService {
    String CONTROLLER = "movie";
    String POPULAR = "popular";
    String TOP_RATED = "top_rated";
    String NOW_PLAYING = "now_playing";
    String UPCOMING = "upcoming";

    @GET(CONTROLLER + "/" + TOP_RATED)
    Observable<MoviesResponse> getTopRatedMovies();

    @GET(CONTROLLER + "/" + POPULAR)
    Observable<MoviesResponse> getPopularMovies();


    @GET(CONTROLLER + "/" + NOW_PLAYING)
    Observable<MoviesResponse> getNowPlayingMovies();

    @GET(CONTROLLER + "/" + UPCOMING)
    Observable<MoviesResponse> getUpcomingMovies();

}
