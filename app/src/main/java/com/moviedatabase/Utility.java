package com.moviedatabase;

/**
 * Created by lucas on 10/10/16.
 */

public class Utility {
    private static final String HTTP_IMAGE_TMDB_ORG_T_P_W342_S = "http://image.tmdb.org/t/p/w342/%s";

    public static String getFullPosterPath(String posterPath) {
        return String.format(HTTP_IMAGE_TMDB_ORG_T_P_W342_S, posterPath);
    }
}
