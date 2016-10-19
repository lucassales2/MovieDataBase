package com.moviedatabase.modules;

import android.content.Context;

import com.moviedatabase.sync.SyncMovie;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lucas on 19/10/16.
 */
@Module
public class SyncModule {

    private Context context;

    public SyncModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @Singleton
    SyncMovie provideSyncMovie() {
        return new SyncMovie(context);
    }
}
