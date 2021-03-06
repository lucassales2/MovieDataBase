package com.moviedatabase.binding.modules;

import android.content.Context;

import com.moviedatabase.R;
import com.moviedatabase.networking.ServiceGenerator;
import com.moviedatabase.networking.movies.MovieApiService;
import com.moviedatabase.networking.movies.MovieDetailApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 09/10/16.
 */
@Module
public class NetworkModule {

    private Context context;

    public NetworkModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @Singleton
    MovieApiService provideMovieApiService() {
        return ServiceGenerator.createService(MovieApiService.class, context.getString(R.string.apiV3));
    }

    @Provides
    @Singleton
    MovieDetailApiService provideMovieDetailsApiService() {
        return ServiceGenerator.createService(MovieDetailApiService.class, context.getString(R.string.apiV3), false);
    }

}
