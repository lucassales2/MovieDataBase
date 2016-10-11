package com.moviedatabase.networking.movies;

import com.moviedatabase.networking.movies.dto.MovieDetailsDto;
import com.moviedatabase.networking.movies.responses.MoviesResponse;
import com.moviedatabase.networking.movies.responses.ReviewResponse;
import com.moviedatabase.networking.movies.responses.VideoResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
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
    String VIDEOS = "videos";
    String MOVIE_ID = "movie_id";
    String REVIEWS = "reviews";

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

    @GET(CONTROLLER + "/{" + MOVIE_ID + "}")
    Observable<MovieDetailsDto> getMovieDetails(@Path(MOVIE_ID) int movieId);


    @GET(CONTROLLER + "/{" + MOVIE_ID + "}/" + VIDEOS)
    Observable<VideoResponse> getVideosFromMovieId(@Path(MOVIE_ID) int movieId);

    @GET(CONTROLLER + "/{" + MOVIE_ID + "}/" + REVIEWS)
    Observable<ReviewResponse> getReviewsFromMovieId(@Path(MOVIE_ID) int movieId);


}
