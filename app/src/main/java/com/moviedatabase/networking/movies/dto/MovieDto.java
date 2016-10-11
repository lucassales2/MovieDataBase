package com.moviedatabase.networking.movies.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

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
    private String poster_path;
    private boolean adult;
    private String overview;
    private String release_date;
    private List<Integer> genre_ids;
    private int id;
    private String original_title;
    private String original_language;
    private String title;
    private String backdrop_path;
    private double popularity;
    private int vote_count;
    private boolean video;
    private float vote_average;

    public MovieDto() {
    }

    protected MovieDto(Parcel in) {
        this.poster_path = in.readString();
        this.adult = in.readByte() != 0;
        this.overview = in.readString();
        this.release_date = in.readString();
        this.genre_ids = new ArrayList<Integer>();
        in.readList(this.genre_ids, Integer.class.getClassLoader());
        this.id = in.readInt();
        this.original_title = in.readString();
        this.original_language = in.readString();
        this.title = in.readString();
        this.backdrop_path = in.readString();
        this.popularity = in.readDouble();
        this.vote_count = in.readInt();
        this.video = in.readByte() != 0;
        this.vote_average = in.readFloat();
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public int getId() {
        return id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdrop_path() {
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

    public float getVote_average() {
        return vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.poster_path);
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
        dest.writeList(this.genre_ids);
        dest.writeInt(this.id);
        dest.writeString(this.original_title);
        dest.writeString(this.original_language);
        dest.writeString(this.title);
        dest.writeString(this.backdrop_path);
        dest.writeDouble(this.popularity);
        dest.writeInt(this.vote_count);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.vote_average);
    }
}
