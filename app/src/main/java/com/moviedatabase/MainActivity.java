package com.moviedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.moviedatabase.sync.MovieSyncAdapter;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements MovieListFragment.Callback {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment(), MovieDetailFragment.TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
        MovieListFragment movieListFragment = (MovieListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movie_listing);
        movieListFragment.setTwoPane(mTwoPane);
        MovieSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public void onItemSelected(long movieId) {
        Observable.just(movieId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long id) {
                        if (mTwoPane) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.movie_detail_container, MovieDetailFragment.newInstance(id), DETAILFRAGMENT_TAG)
                                    .commit();
                        } else {
                            Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                            intent.putExtra(MovieDetailActivity.MOVIE_ID, id);
                            startActivity(intent);
                        }
                    }
                });
    }
}
