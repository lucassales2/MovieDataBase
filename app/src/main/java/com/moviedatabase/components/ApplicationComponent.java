package com.moviedatabase.components;

import com.moviedatabase.MovieListFragment;
import com.moviedatabase.modules.AppModule;
import com.moviedatabase.modules.NetworkModule;
import com.moviedatabase.modules.SyncModule;
import com.moviedatabase.presenters.MovieDetailPresenter;
import com.moviedatabase.presenters.MovieListPresenter;
import com.moviedatabase.sync.MovieSyncAdapter;
import com.moviedatabase.sync.SyncMovie;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by lucas on 09/10/16.
 */
@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, SyncModule.class})
public interface ApplicationComponent {

    void inject(MovieListPresenter presenter);

    void inject(MovieDetailPresenter presenter);

    void inject(MovieSyncAdapter syncAdapter);

    void inject(MovieListFragment fragment);

    void inject(SyncMovie syncMovie);
}