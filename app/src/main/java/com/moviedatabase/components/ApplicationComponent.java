package com.moviedatabase.components;

import com.moviedatabase.MovieListFragment;
import com.moviedatabase.modules.AppModule;
import com.moviedatabase.modules.NetworkModule;
import com.moviedatabase.sync.MovieSyncAdapter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by lucas on 09/10/16.
 */
@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface ApplicationComponent {

    void inject(MovieSyncAdapter syncAdapter);

    void inject(MovieListFragment fragment);

}