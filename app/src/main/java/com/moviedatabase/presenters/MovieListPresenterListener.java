package com.moviedatabase.presenters;

import com.moviedatabase.networking.movies.dto.MovieDto;

import java.util.List;

/**
 * Created by lucas on 11/10/16.
 */

public interface MovieListPresenterListener {
    void onMoviesLoaded(List<MovieDto> results);

    void onTitleChanged(String title);
}
