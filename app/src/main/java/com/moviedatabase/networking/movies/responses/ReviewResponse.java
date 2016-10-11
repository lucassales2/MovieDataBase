package com.moviedatabase.networking.movies.responses;

import com.moviedatabase.networking.movies.dto.ReviewDto;

import java.util.List;

/**
 * Created by lucas on 10/10/16.
 */
public class ReviewResponse {
    public int id;
    public int page;
    public List<ReviewDto> results;
    public int total_pages;
    public int total_results;
}
