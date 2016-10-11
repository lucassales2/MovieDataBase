package com.moviedatabase.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moviedatabase.BR;
import com.moviedatabase.networking.movies.dto.MovieDto;

import java.util.Locale;

import static com.moviedatabase.Utility.HTTP_IMAGE_TMDB_ORG_T_P_W342_S;

/**
 * Created by lucas on 10/10/16.
 */

public class MovieDetailViewModel extends BaseObservable {
    public String originalTitle;
    private String title;
    private String posterPath;
    private String releaseDate;
    private float rating;
    private String overview;
    private String runtime;

    public MovieDetailViewModel(MovieDto movieDto) {
        this.title = movieDto.getTitle();
        posterPath = String.format(HTTP_IMAGE_TMDB_ORG_T_P_W342_S, movieDto.getPoster_path());
        releaseDate = movieDto.getRelease_date();
        rating = movieDto.getVote_average();
        overview = movieDto.getOverview();
        originalTitle = title;
        runtime = "";
    }

    @BindingAdapter("imgUrl")
    public static void bindImgUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    @Bindable
    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
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

    public void setRuntime(Integer runtime) {
        this.runtime = String.format("%dmin", runtime);
        notifyPropertyChanged(BR.runtime);
    }
}
