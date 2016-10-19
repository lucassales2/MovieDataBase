package com.moviedatabase.networking.movies.dto;

import java.util.List;

/**
 * Created by lucas on 10/10/16.
 */

public class MovieDetailsDto extends MovieDto {

    private Integer budget;
    private List<IdName> genres;
    private String homepage;
    private String imdb_id;
    private List<IdName> production_companies;
    private List<ProductionCompany> production_countries;
    private Integer revenue;
    private Integer runtime;
    private List<SpokenLanguage> spoken_languages;
    private String status;
    private String tagline;

    public Integer getBudget() {
        return budget;
    }

    public List<IdName> getGenres() {
        return genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getImdbId() {
        return imdb_id;
    }

    public List<IdName> getProductionCompanies() {
        return production_companies;
    }

    public List<ProductionCompany> getProductionCountries() {
        return production_countries;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public List<SpokenLanguage> getSpokenLanguages() {
        return spoken_languages;
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

    public static class IdName {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public static class ProductionCompany {
        private String iso_3166_1;
        private String name;

        public String getIso_3166_1() {
            return iso_3166_1;
        }

        public String getName() {
            return name;
        }
    }

    public static class SpokenLanguage {
        private String iso_639_1;
        private String name;

        public String getIso_639_1() {
            return iso_639_1;
        }

        public String getName() {
            return name;
        }
    }
}
