package com.moviedatabase.presenters;

import com.moviedatabase.networking.movies.dto.MovieDetailsDto;
import com.moviedatabase.networking.movies.dto.ReviewDto;
import com.moviedatabase.networking.movies.dto.VideoDto;

import java.util.List;

/**
 * Created by lucas on 11/10/16.
 */

public interface MovieDetailPresenterListener {

    void onMovieDetailsLoaded(MovieDetailsDto movieDetailsDto);

    void onVideosLoaded(List<VideoDto> results);

    void onReviewsLoaded(List<ReviewDto> results);
}
