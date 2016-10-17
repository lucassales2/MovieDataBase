package com.moviedatabase.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moviedatabase.BR;
import com.moviedatabase.Utility;
import com.moviedatabase.networking.movies.dto.MovieDto;
import com.moviedatabase.networking.movies.dto.ReviewDto;
import com.moviedatabase.networking.movies.dto.VideoDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by lucas on 10/10/16.
 */

public class MovieDetailViewModel extends BaseObservable implements Parcelable {
    public static final Parcelable.Creator<MovieDetailViewModel> CREATOR = new Parcelable.Creator<MovieDetailViewModel>() {
        @Override
        public MovieDetailViewModel createFromParcel(Parcel source) {
            return new MovieDetailViewModel(source);
        }

        @Override
        public MovieDetailViewModel[] newArray(int size) {
            return new MovieDetailViewModel[size];
        }
    };
    private String originalTitle;
    private String title;
    private String posterPath;
    private String releaseDate;
    private float rating;
    private String overview;
    private String runtime;
    private ArrayList<VideoDto> videoDtoList;
    private ArrayList<ReviewDto> reviewDtos;

    public MovieDetailViewModel(MovieDto movieDto) {
        this.title = movieDto.getTitle();
        posterPath = Utility.getFullPosterPath(movieDto.getPosterPath());
        releaseDate = movieDto.getReleaseDate();
        rating = movieDto.getVoteAverage();
        overview = movieDto.getOverview();
        originalTitle = movieDto.getOriginalTitle();
        videoDtoList = new ArrayList<>();
        reviewDtos = new ArrayList<>();
        runtime = "";
    }

    protected MovieDetailViewModel(Parcel in) {
        this.originalTitle = in.readString();
        this.title = in.readString();
        this.posterPath = in.readString();
        this.releaseDate = in.readString();
        this.rating = in.readFloat();
        this.overview = in.readString();
        this.runtime = in.readString();
        this.videoDtoList = new ArrayList<VideoDto>();
        in.readList(this.videoDtoList, VideoDto.class.getClassLoader());
        this.reviewDtos = in.createTypedArrayList(ReviewDto.CREATOR);
    }

    @BindingAdapter("imgUrl")
    public static void bindImgUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    public ArrayList<VideoDto> getVideos() {
        return videoDtoList;
    }

    public List<ReviewDto> getReviews() {
        return reviewDtos;
    }

    public void addVideos(List<VideoDto> videoDtoList) {
        this.videoDtoList.addAll(videoDtoList);
    }

    public void addReviews(List<ReviewDto> reviewDtos) {
        this.reviewDtos.addAll(reviewDtos);
    }

    @Bindable
    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = String.format("(%s)", originalTitle);
        notifyPropertyChanged(BR.originalTitle);
    }

    public String getReleaseDate() {
        return releaseDate.substring(0, 4);
    }

    public String getRating() {
        return String.format(Locale.getDefault(), "%.1f/10", rating);
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    @Bindable
    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = String.format("%d min", runtime);
        notifyPropertyChanged(BR.runtime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.originalTitle);
        dest.writeString(this.title);
        dest.writeString(this.posterPath);
        dest.writeString(this.releaseDate);
        dest.writeFloat(this.rating);
        dest.writeString(this.overview);
        dest.writeString(this.runtime);
        dest.writeList(this.videoDtoList);
        dest.writeTypedList(this.reviewDtos);
    }
}
