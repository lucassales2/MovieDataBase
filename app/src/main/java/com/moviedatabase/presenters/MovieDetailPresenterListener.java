package com.moviedatabase.presenters;

import com.moviedatabase.networking.movies.dto.MovieDetailsDto;

/**
 * Created by lucas on 11/10/16.
 */

public interface MovieDetailPresenterListener {

    void onMovieDetailsLoaded(MovieDetailsDto movieDetailsDto);
}
