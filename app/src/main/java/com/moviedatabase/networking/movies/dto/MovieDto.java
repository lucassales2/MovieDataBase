package com.moviedatabase.networking.movies.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by lucas on 27/09/16.
 */

public class MovieDto {
    private String poster_path;
    private boolean adult;
    private String overview;
    private String release_date;
    private List<Integer> genre_ids;
    private long id;
    private String original_title;
    private String original_language;
    private String title;
    private String backdrop_path;
    private double popularity;
    private int vote_count;
    private boolean video;
    private float vote_average;

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public String getReleaseDateInMillis() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(release_date);
        } catch (ParseException e) {
            date = new Date(0L);
        }
        return String.valueOf(date.getTime());
    }

    public List<Integer> getGenreIds() {
        return genre_ids;
    }

    public long getId() {
        return id;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public String getOriginalLanguage() {
        return original_language;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public double getPopularity() {
        return popularity;
    }

    public int getVote_count() {
        return vote_count;
    }

    public boolean isVideo() {
        return video;
    }

    public float getVoteAverage() {
        return vote_average;
    }

    public String getPosterPath() {
        return poster_path;
    }
}
