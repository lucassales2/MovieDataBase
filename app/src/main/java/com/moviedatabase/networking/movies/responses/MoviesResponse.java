package com.moviedatabase.networking.movies.responses;

import com.moviedatabase.networking.movies.dto.MovieDto;

import java.util.List;

/**
 * Created by lucas on 27/09/16.
 */

public class MoviesResponse {
    public int page;
    public List<MovieDto> results;
    public int total_results;
    public int total_pages;
}
