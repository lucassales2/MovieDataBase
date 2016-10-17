package com.moviedatabase.networking.movies.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lucas on 10/10/16.
 */

public class VideoDto implements Parcelable {
    public static final Parcelable.Creator<VideoDto> CREATOR = new Parcelable.Creator<VideoDto>() {
        @Override
        public VideoDto createFromParcel(Parcel source) {
            return new VideoDto(source);
        }

        @Override
        public VideoDto[] newArray(int size) {
            return new VideoDto[size];
        }
    };
    private String id;
    private String iso_639_1;
    private String iso_3166_1;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public VideoDto() {
    }

    protected VideoDto(Parcel in) {
        this.id = in.readString();
        this.iso_639_1 = in.readString();
        this.iso_3166_1 = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.size = in.readInt();
        this.type = in.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.iso_639_1);
        dest.writeString(this.iso_3166_1);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeInt(this.size);
        dest.writeString(this.type);
    }
}
