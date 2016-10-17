package com.moviedatabase.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moviedatabase.R;
import com.moviedatabase.networking.movies.dto.VideoDto;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 11/10/16.
 */

public class TrailersAdaper extends RecyclerView.Adapter<TrailersAdaper.ViewHolder> {

    private ArrayList<VideoDto> videoList;
    private WeakReference<TrailersAdapterListener> listener;

    public TrailersAdaper(List<VideoDto> videoList, TrailersAdapterListener listener) {
        this.videoList = new ArrayList<>(videoList);
        this.listener = new WeakReference<>(listener);
    }

    public TrailersAdaper(TrailersAdapterListener listener) {
        this.listener = new WeakReference<>(listener);
        this.videoList = new ArrayList<>();
    }

    public void add(VideoDto videoDto) {
        videoList.add(videoDto);
        notifyItemInserted(videoList.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final VideoDto videoDto = videoList.get(position);
        holder.textView.setText(String.format("Trailer %d", position + 1));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.get().onVideoClick(videoDto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;

        private ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textViewTrailer);
        }
    }
}
