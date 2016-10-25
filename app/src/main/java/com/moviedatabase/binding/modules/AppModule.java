package com.moviedatabase.binding.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.moviedatabase.MovieApplication;
import com.moviedatabase.binding.annotations.ForApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 09/10/16.
 */

@Module
public class AppModule {
    private final MovieApplication application;

    public AppModule(MovieApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    @ForApplication
    Context provideApplicationContext() {
        return application;
    }


    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }


}
