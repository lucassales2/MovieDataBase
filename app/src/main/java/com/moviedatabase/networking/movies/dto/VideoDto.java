package com.moviedatabase.networking.movies.dto;

/**
 * Created by lucas on 10/10/16.
 */

public class VideoDto {
    private String id;
    private String iso_639_1;
    private String iso_3166_1;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public String getId() {
        return id;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}
