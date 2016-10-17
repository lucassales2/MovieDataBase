package com.moviedatabase.networking.movies;

import com.moviedatabase.networking.movies.dto.MovieDetailsDto;
import com.moviedatabase.networking.movies.responses.ReviewResponse;
import com.moviedatabase.networking.movies.responses.VideoResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

import static com.moviedatabase.networking.movies.Constants.CONTROLLER;
import static com.moviedatabase.networking.movies.Constants.MOVIE_ID;
import static com.moviedatabase.networking.movies.Constants.REVIEWS;
import static com.moviedatabase.networking.movies.Constants.VIDEOS;

/**
 * Created by lucas on 17/10/16.
 */

public interface MovieDetailApiService {

    @GET(CONTROLLER + "/{" + MOVIE_ID + "}")
    Observable<MovieDetailsDto> getMovieDetails(@Path(MOVIE_ID) int movieId);


    @GET(CONTROLLER + "/{" + MOVIE_ID + "}/" + VIDEOS)
    Observable<VideoResponse> getVideosFromMovieId(@Path(MOVIE_ID) int movieId);

    @GET(CONTROLLER + "/{" + MOVIE_ID + "}/" + REVIEWS)
    Observable<ReviewResponse> getReviewsFromMovieId(@Path(MOVIE_ID) int movieId);
}
