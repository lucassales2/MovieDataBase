package com.moviedatabase.networking.movies.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lucas on 10/10/16.
 */

public class ReviewDto implements Parcelable {
    public static final Parcelable.Creator<ReviewDto> CREATOR = new Parcelable.Creator<ReviewDto>() {
        @Override
        public ReviewDto createFromParcel(Parcel source) {
            return new ReviewDto(source);
        }

        @Override
        public ReviewDto[] newArray(int size) {
            return new ReviewDto[size];
        }
    };
    private String id;
    private String author;
    private String content;
    private String url;

    public ReviewDto() {
    }

    protected ReviewDto(Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.url);
    }
}
