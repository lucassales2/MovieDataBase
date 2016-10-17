package com.moviedatabase.networking.movies.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lucas on 27/09/16.
 */

public class MovieDto implements Parcelable {

    public static final Parcelable.Creator<MovieDto> CREATOR = new Parcelable.Creator<MovieDto>() {
        @Override
        public MovieDto createFromParcel(Parcel source) {
            return new MovieDto(source);
        }

        @Override
        public MovieDto[] newArray(int size) {
            return new MovieDto[size];
        }
    };
    private int id;
    private String poster_path;
    private String overview;
    private String release_date;
    private String original_title;
    private String original_language;
    private String title;
    private float vote_average;

    public MovieDto() {
    }

    protected MovieDto(Parcel in) {
        this.id = in.readInt();
        this.poster_path = in.readString();
        this.overview = in.readString();
        this.release_date = in.readString();
        this.original_title = in.readString();
        this.original_language = in.readString();
        this.title = in.readString();
        this.vote_average = in.readFloat();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public float getVoteAverage() {
        return vote_average;
    }

    public String getPosterPath() {
        return poster_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.poster_path);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
        dest.writeString(this.original_title);
        dest.writeString(this.original_language);
        dest.writeString(this.title);
        dest.writeFloat(this.vote_average);
    }
}
