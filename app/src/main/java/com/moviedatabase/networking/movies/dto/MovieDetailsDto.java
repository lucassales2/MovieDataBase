package com.moviedatabase.networking.movies.dto;

import java.util.List;

/**
 * Created by lucas on 10/10/16.
 */

public class MovieDetailsDto {
    public Boolean adult;
    public String backdrop_path;
    public Integer budget;
    public List<IdName> genres;
    public String homepage;
    public Integer id;
    public String imdb_id;
    public String original_language;
    public String original_title;
    public String overview;
    public Double popularity;
    public String poster_path;
    public List<IdName> production_companies;
    public List<ProductionCompany> production_countries;
    public String release_date;
    public Integer revenue;
    public Integer runtime;
    public List<SpokenLanguage> spoken_languages;
    public String status;
    public String tagline;
    public String title;
    public Boolean video;
    public Float vote_average;
    public Integer vote_count;


    public static class IdName {
        public int id;
        public String name;
    }

    public static class ProductionCompany {
        String iso_3166_1;
        String name;
    }

    public static class SpokenLanguage {
        String iso_639_1;
        String name;
    }
}
