package com.moviedatabase;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.moviedatabase.adapters.MoviesCursorAdapter;
import com.moviedatabase.adapters.MoviesCursorAdapterListener;
import com.moviedatabase.data.MovieContract;
import com.moviedatabase.sync.MovieSyncAdapter;

import javax.inject.Inject;

/**
 * Created by lucas on 07/10/16.
 */

public class MovieListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, MoviesCursorAdapterListener, SharedPreferences.OnSharedPreferenceChangeListener {
    public final static String TAG = MovieListFragment.class.getSimpleName();
    public static final int COL_ID = 0;
    public static final int COL_POSTER_PATH = 1;

    private final String SORT_ORDER = "sortOrder";
    private final String SELECTION = "selection";
    private final String SELECTION_ARGS = "selectionArgs";
    private final String LAST_SELECTED = "lastSelected";
    @Inject
    SharedPreferences sharedPreferences;
    private int lastSelected = 0;
    private MoviesCursorAdapter cursorAdapter;
    private Callback callback;
    private int LOADER_ID = 0;
    private GridLayoutManager gridLayoutManager;
    private boolean twoPane;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (Callback) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MovieApplication.getInstance().getComponent().inject(this);
        if (savedInstanceState == null) {
            MovieSyncAdapter.syncImmediately(getContext());
        } else {
            lastSelected = savedInstanceState.getInt(LAST_SELECTED, 0);
        }
        getLoaderManager().initLoader(LOADER_ID, createLoaderArguments(), this);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView.setHasFixedSize(true);
        String title = sharedPreferences.getString(getString(R.string.movie_option), getString(R.string.top_rated));
        getActivity().setTitle(title);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        cursorAdapter = new MoviesCursorAdapter(null, this);
        recyclerView.setAdapter(cursorAdapter);
        return recyclerView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(LAST_SELECTED, lastSelected);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        switch (item.getItemId()) {
            case R.id.action_popular:
                edit.putString(getString(R.string.movie_option), getString(R.string.popular)).apply();
                break;
            case R.id.action_now_playing:
                edit.putString(getString(R.string.movie_option), getString(R.string.now_playing)).apply();
                break;
            case R.id.action_upcoming:
                edit.putString(getString(R.string.movie_option), getString(R.string.upcoming)).apply();
                break;
            case R.id.action_top_rated:
                edit.putString(getString(R.string.movie_option), getString(R.string.top_rated)).apply();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTwoPane(boolean twoPane) {
        this.twoPane = twoPane;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry._ID, MovieContract.MovieEntry.COLUMN_POSTER_PATH},
                args.getString(SELECTION, null),
                args.getStringArray(SELECTION_ARGS),
                args.getString(SORT_ORDER));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
        if (twoPane && cursor.moveToPosition(lastSelected)) {
            callback.onItemSelected(cursor.getLong(0));
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(long id, int position) {
        lastSelected = position;
        callback.onItemSelected(id);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.movie_option))) {
            MovieSyncAdapter.syncImmediately(getContext());
            getActivity().setTitle(sharedPreferences.getString(getString(R.string.movie_option), getString(R.string.top_rated)));
            getLoaderManager().restartLoader(LOADER_ID, createLoaderArguments(), this);
        }
    }

    private Bundle createLoaderArguments() {
        String movieOption = sharedPreferences.getString(getString(R.string.movie_option), getString(R.string.top_rated));
        Bundle bundle = new Bundle();
        if (movieOption.equals(getString(R.string.top_rated))) {
            bundle.putString(SORT_ORDER, MovieContract.MovieEntry.COLUMN_RATING + " DESC");
            bundle.putString(SELECTION, MovieContract.MovieEntry.COLUMN_RELEASE_DATE + "<= ?");
            bundle.putStringArray(SELECTION_ARGS, new String[]{String.valueOf(System.currentTimeMillis() - 7889238000L)});
        } else if (movieOption.equals(getString(R.string.popular))) {
            bundle.putString(SORT_ORDER, MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC");
            bundle.putString(SELECTION, null);
            bundle.putStringArrayList(SELECTION_ARGS, null);
        } else if (movieOption.equals(getString(R.string.now_playing))) {
            bundle.putString(SORT_ORDER, MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " DESC");
            bundle.putString(SELECTION, MovieContract.MovieEntry.COLUMN_RELEASE_DATE + "<= ?");
            bundle.putStringArray(SELECTION_ARGS, new String[]{String.valueOf(System.currentTimeMillis())});
        } else {
            bundle.putString(SORT_ORDER, MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " ASC");
            bundle.putString(SELECTION, MovieContract.MovieEntry.COLUMN_RELEASE_DATE + ">= ?");
            bundle.putStringArray(SELECTION_ARGS, new String[]{String.valueOf(System.currentTimeMillis())});
        }
        return bundle;
    }

    public interface Callback {
        void onItemSelected(long movieId);
    }
}
