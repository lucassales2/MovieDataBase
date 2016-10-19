package com.moviedatabase;

import android.app.Application;

import com.moviedatabase.components.ApplicationComponent;
import com.moviedatabase.components.DaggerApplicationComponent;
import com.moviedatabase.modules.AppModule;
import com.moviedatabase.modules.NetworkModule;
import com.moviedatabase.modules.SyncModule;

/**
 * Created by lucas on 27/09/16.
 */

public class MovieApplication extends Application {

    private static MovieApplication instance;
    private ApplicationComponent component;
    public static MovieApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        component = DaggerApplicationComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule(this))
                .syncModule(new SyncModule(this))
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }

}
