package com.moviedatabase.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moviedatabase.Utility;

import java.util.Locale;

/**
 * Created by lucas on 10/10/16.
 */

public class MovieDetailViewModel extends BaseObservable {

    private String originalTitle;
    private String title;
    private String posterPath;
    private String releaseDate;
    private float rating;
    private String overview;
    private String runtime;
    private boolean isFavorite;

    public MovieDetailViewModel(String originalTitle, String overview, String posterPath, float rating, String releaseDate, String runtime, String title, boolean isFavorite) {
        this.isFavorite = isFavorite;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.posterPath = posterPath;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.title = title;
    }

    @BindingAdapter("imgUrl")
    public static void bindImgUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(Utility.getFullPosterPath(url)).into(imageView);
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    @Bindable
    public String getOriginalTitle() {
        return originalTitle;
    }

    @Bindable
    public String getFormattedOriginalTitle() {
        return String.format("(%s)", getOriginalTitle());
    }

    public String getReleaseDate() {
        return releaseDate;
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

    public String getRuntime() {
        return String.format("%s min", runtime);
    }

}
