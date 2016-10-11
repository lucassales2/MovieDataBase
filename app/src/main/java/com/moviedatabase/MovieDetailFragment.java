package com.moviedatabase;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moviedatabase.networking.movies.dto.MovieDetailsDto;
import com.moviedatabase.networking.movies.dto.MovieDto;
import com.moviedatabase.presenters.MovieDetailPresenter;
import com.moviedatabase.presenters.MovieDetailPresenterListener;
import com.moviedatabase.viewmodels.MovieDetailViewModel;

import java.util.Locale;

/**
 * Created by lucas on 10/10/16.
 */

public class MovieDetailFragment extends Fragment implements MovieDetailPresenterListener {
    public static final String TAG = MovieListFragment.class.getSimpleName();
    private static final String MOVIE_KEY = "movie";
    private ViewDataBinding dataBinding;
    private MovieDetailPresenter presenter;
    private MovieDto movieDto;
    private MovieDetailViewModel viewModel;

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
        movieDto = getArguments().getParcelable(MOVIE_KEY);
        presenter = new MovieDetailPresenter(getContext(), movieDto.getId(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        viewModel = new MovieDetailViewModel(movieDto);
        dataBinding.setVariable(BR.movie, viewModel);
        presenter.fetchMovieDetails();
        return dataBinding.getRoot();
    }

    @Override
    public void onMovieDetailsLoaded(MovieDetailsDto movieDetailsDto) {
        if (!movieDetailsDto.original_language.equals(Locale.getDefault().getLanguage())) {
            viewModel.setOriginalTitle(String.format("(%s)", movieDetailsDto.original_title));
            viewModel.setRuntime(movieDetailsDto.runtime);
        }
    }
}
