package com.moviedatabase;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moviedatabase.adapters.ReviewsAdapter;
import com.moviedatabase.adapters.ReviewsAdapterListener;
import com.moviedatabase.adapters.TrailersAdaper;
import com.moviedatabase.adapters.TrailersAdapterListener;
import com.moviedatabase.networking.movies.dto.MovieDetailsDto;
import com.moviedatabase.networking.movies.dto.MovieDto;
import com.moviedatabase.networking.movies.dto.ReviewDto;
import com.moviedatabase.networking.movies.dto.VideoDto;
import com.moviedatabase.presenters.MovieDetailPresenter;
import com.moviedatabase.presenters.MovieDetailPresenterListener;
import com.moviedatabase.viewmodels.MovieDetailViewModel;

import java.util.List;
import java.util.Locale;

/**
 * Created by lucas on 10/10/16.
 */

public class MovieDetailFragment extends Fragment implements MovieDetailPresenterListener, TrailersAdapterListener, ReviewsAdapterListener {
    public static final String TAG = MovieListFragment.class.getSimpleName();
    private static final String MOVIE_VM_KEY = "movieVM";
    private static final String MOVIE_KEY = "movie";

    private MovieDetailPresenter presenter;
    private MovieDetailViewModel viewModel;
    private TrailersAdaper trailersAdaper;
    private ReviewsAdapter reviewsAdapter;
    private RecyclerView recyclerViewVideos;
    private RecyclerView recyclerViewReviews;
    private TextView textViewReview;
    private TextView textViewTrailer;
    private View dividerReviews;
    private View dividerTrailers;
    private MovieDto movieDto;

    public static MovieDetailFragment newInstance(MovieDto movieDto) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_KEY, movieDto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(MOVIE_KEY)) {
            movieDto = getArguments().getParcelable(MOVIE_KEY);
            assert movieDto != null;
            presenter = new MovieDetailPresenter(movieDto.getId(), this);
        } else {
            throw new IllegalArgumentException("Start fragment with newInstance static method");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        View rootView = dataBinding.getRoot();
        textViewReview = (TextView) rootView.findViewById(R.id.textViewReview);
        textViewTrailer = (TextView) rootView.findViewById(R.id.textViewTrailer);
        recyclerViewVideos = (RecyclerView) rootView.findViewById(R.id.recyclerView_videos);
        recyclerViewReviews = (RecyclerView) rootView.findViewById(R.id.recyclerView_reviews);
        dividerReviews = rootView.findViewById(R.id.divider_reviews);
        dividerTrailers = rootView.findViewById(R.id.divider_trailers);
        recyclerViewReviews.setNestedScrollingEnabled(false);
        recyclerViewVideos.setNestedScrollingEnabled(false);
        recyclerViewVideos.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(getContext()));

        if (savedInstanceState == null) {
            trailersAdaper = new TrailersAdaper(this);
            recyclerViewVideos.setAdapter(trailersAdaper);
            reviewsAdapter = new ReviewsAdapter(this);
            recyclerViewReviews.setAdapter(reviewsAdapter);
            viewModel = new MovieDetailViewModel(movieDto);
            presenter.init();
        } else {
            viewModel = savedInstanceState.getParcelable(MOVIE_VM_KEY);
            trailersAdaper = new TrailersAdaper(viewModel.getVideos(), this);
            recyclerViewVideos.setAdapter(trailersAdaper);
            reviewsAdapter = new ReviewsAdapter(viewModel.getReviews(), this);
            recyclerViewReviews.setAdapter(reviewsAdapter);
        }
        dataBinding.setVariable(BR.movie, viewModel);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MOVIE_VM_KEY, viewModel);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMovieDetailsLoaded(MovieDetailsDto movieDetailsDto) {
        if (!movieDetailsDto.getOriginal_language().equals(Locale.getDefault().getLanguage()) && !movieDetailsDto.getTitle().equals(movieDetailsDto.getOriginal_title())) {
            viewModel.setOriginalTitle(movieDetailsDto.getOriginal_title());
        }
        viewModel.setRuntime(movieDetailsDto.getRuntime());
    }

    @Override
    public void onVideosLoaded(List<VideoDto> results) {
        if (!results.isEmpty()) {
            dividerTrailers.setVisibility(View.VISIBLE);
            textViewTrailer.setVisibility(View.VISIBLE);
            recyclerViewVideos.setVisibility(View.VISIBLE);
            viewModel.addVideos(results);
            for (VideoDto videoDto : viewModel.getVideos()) {
                trailersAdaper.add(videoDto);
            }
        }
    }

    @Override
    public void onReviewsLoaded(List<ReviewDto> results) {
        if (!results.isEmpty()) {
            dividerReviews.setVisibility(View.VISIBLE);
            textViewReview.setVisibility(View.VISIBLE);
            recyclerViewReviews.setVisibility(View.VISIBLE);
            viewModel.addReviews(results);
            for (ReviewDto reviewDto : viewModel.getReviews()) {
                reviewsAdapter.add(reviewDto);
            }
        }
    }

    @Override
    public void onVideoClick(VideoDto videoDto) {
        if (videoDto.getSite().equals(getString(R.string.you_tube))) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("http://www.youtube.com/watch?v=%s", videoDto.getKey()))));
        }
    }

    @Override
    public void onReviewClick(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}
