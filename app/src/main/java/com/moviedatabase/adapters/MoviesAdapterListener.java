package com.moviedatabase.adapters;

import com.moviedatabase.networking.movies.dto.MovieDto;

/**
 * Created by lucas on 28/09/16.
 */

public interface MoviesAdapterListener {
    void onItemClick(MovieDto movieDto);
}
