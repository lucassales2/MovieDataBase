package com.moviedatabase.adapters;

/**
 * Created by lucas on 17/10/16.
 */

public interface VideosCursorAdapterListener {
    void onVideoClick(String key);

    void onShareClick(String key, String name);
}
