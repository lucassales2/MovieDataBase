package com.moviedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.moviedatabase.adapters.MoviesAdapter;
import com.moviedatabase.adapters.MoviesAdapterListener;
import com.moviedatabase.networking.movies.dto.MovieDto;
import com.moviedatabase.presenters.MovieListPresenter;
import com.moviedatabase.presenters.MovieListPresenterListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 07/10/16.
 */

public class MovieListFragment extends Fragment implements MoviesAdapterListener, MovieListPresenterListener {
    public final static String TAG = MovieListFragment.class.getSimpleName();
    private final String MOVIES = "movies";
    private MoviesAdapter adapter;
    private MovieListPresenter presenter;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIES, adapter.getMovies());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MovieListPresenter(getContext(), this);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        String title = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getString(R.string.movie_option), getString(R.string.top_rated));
        getActivity().setTitle(title);
        if (savedInstanceState == null) {
            presenter.fetchMovies(title);
            adapter = new MoviesAdapter(this);
        } else {
            ArrayList<MovieDto> movies = savedInstanceState.getParcelableArrayList(MOVIES);
            adapter = new MoviesAdapter(movies, this);
        }
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        adapter.clear();
        switch (item.getItemId()) {
            case R.id.action_popular:
                presenter.fetchMovies(getString(R.string.popular));
                break;
            case R.id.action_now_playing:
                presenter.fetchMovies(getString(R.string.now_playing));
                break;
            case R.id.action_upcoming:
                presenter.fetchMovies(getString(R.string.upcoming));
                break;
            case R.id.action_top_rated:
                presenter.fetchMovies(getString(R.string.top_rated));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(MovieDto movieDto) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.MOVIE, movieDto);
        startActivity(intent);
    }

    @Override
    public void onMoviesLoaded(List<MovieDto> results) {
        for (MovieDto result : results) {
            adapter.add(result);
        }
    }

    @Override
    public void onTitleChanged(String title) {
        getActivity().setTitle(title);
    }
}
