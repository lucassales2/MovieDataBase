package com.moviedatabase;

import android.app.Application;

import com.moviedatabase.binding.components.ApplicationComponent;
import com.moviedatabase.binding.components.DaggerApplicationComponent;
import com.moviedatabase.binding.modules.AppModule;
import com.moviedatabase.binding.modules.NetworkModule;

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
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }

}
