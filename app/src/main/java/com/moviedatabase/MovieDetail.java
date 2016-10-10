package com.moviedatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.moviedatabase.networking.movies.dto.MovieDto;

public class MovieDetail extends AppCompatActivity {

    public final static String MOVIE = "movie";
    private MovieDto movieDto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movieDto = getIntent().getParcelableExtra(MOVIE);
    }
}
