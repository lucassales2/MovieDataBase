package com.moviedatabase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.moviedatabase.networking.movies.MovieApiService;
import com.moviedatabase.networking.movies.dto.MovieDto;
import com.moviedatabase.networking.movies.dto.MoviesResponse;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lucas on 07/10/16.
 */

public class MovieListFragment extends Fragment implements MoviesAdapterListener {
    public final static String TAG = MovieListFragment.class.getSimpleName();
    private final String MOVIES = "movies";
    private final String MOVIE_OPTION = "movieOption";
    @Inject
    MovieApiService movieApiService;
    @Inject
    SharedPreferences sharedPreferences;
    private MoviesAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIES, adapter.getMovies());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MovieApplication.getInstance().getComponent().inject(this);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        String title = sharedPreferences.getString(MOVIE_OPTION, getString(R.string.top_rated));
        if (savedInstanceState == null) {
            fetchMovies(title);
            adapter = new MoviesAdapter(this);
        } else {
            getActivity().setTitle(title);
            ArrayList<MovieDto> movies = savedInstanceState.getParcelableArrayList(MOVIES);
            adapter = new MoviesAdapter(movies, this);
        }
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    private void fetchMovies(String string) {
        Observable<MoviesResponse> moviesResponseObservable;
        getActivity().setTitle(string);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(MOVIE_OPTION, string).apply();

        if (string.equals(getString(R.string.popular))) {
            moviesResponseObservable = movieApiService.getPopularMovies();
        } else if (string.equals(getString(R.string.upcoming))) {
            moviesResponseObservable = movieApiService.getUpcomingMovies();
        } else if (string.equals(getString(R.string.now_playing))) {
            moviesResponseObservable = movieApiService.getNowPlayingMovies();
        } else {
            moviesResponseObservable = movieApiService.getTopRatedMovies();
        }
        if (adapter != null) {
            adapter.clear();
        }
        moviesResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MoviesResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MoviesResponse moviesResponse) {
                        adapter.addAll(moviesResponse.results);
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular:
                fetchMovies(getString(R.string.popular));
                break;
            case R.id.action_now_playing:
                fetchMovies(getString(R.string.now_playing));
                break;
            case R.id.action_upcoming:
                fetchMovies(getString(R.string.upcoming));
                break;
            case R.id.action_top_rated:
                fetchMovies(getString(R.string.top_rated));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(MovieDto movieDto) {
        Intent intent = new Intent(getContext(), MovieDetail.class);
        intent.putExtra(MovieDetail.MOVIE, movieDto);
        startActivity(intent);
    }
}
