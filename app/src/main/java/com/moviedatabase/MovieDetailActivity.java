package com.moviedatabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.moviedatabase.networking.movies.dto.MovieDto;

public class MovieDetailActivity extends AppCompatActivity {

    public final static String MOVIE = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MovieDto movieDto = getIntent().getParcelableExtra(MOVIE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, MovieDetailFragment.newInstance(movieDto), MovieDetailFragment.TAG)
                .commit();
    }
}
