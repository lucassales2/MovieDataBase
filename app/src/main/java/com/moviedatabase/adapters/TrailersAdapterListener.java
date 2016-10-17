package com.moviedatabase.adapters;

import com.moviedatabase.networking.movies.dto.VideoDto;

/**
 * Created by lucas on 17/10/16.
 */

public interface TrailersAdapterListener {
    void onVideoClick(VideoDto videoDto);
}
