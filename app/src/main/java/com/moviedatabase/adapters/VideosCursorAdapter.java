package com.moviedatabase.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moviedatabase.MovieDetailFragment;
import com.moviedatabase.R;
import com.moviedatabase.ui.CursorRecyclerViewAdapter;

import java.lang.ref.WeakReference;

/**
 * Created by lucas on 24/10/16.
 */

public class VideosCursorAdapter extends CursorRecyclerViewAdapter<VideosCursorAdapter.ViewHolder> {

    private WeakReference<VideosCursorAdapterListener> listener;

    public VideosCursorAdapter(Cursor cursor, VideosCursorAdapterListener videosCursorAdapterListener) {
        super(cursor);
        listener = new WeakReference<>(videosCursorAdapterListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        final String name = cursor.getString(MovieDetailFragment.INDEX_VIDEO_COL_NAME);
        final String key = cursor.getString(MovieDetailFragment.INDEX_VIDEO_COL_KEY);
        holder.textView.setText(name);
        Glide.with(holder.itemView.getContext()).load(String.format("http://img.youtube.com/vi/%s/mqdefault.jpg", key)).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.get().onVideoClick(key);
            }
        });
        holder.imageViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.get().onShareClick(key, name);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_row, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;
        final ImageView imageView;
        final ImageView imageViewShare;

        private ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textViewTrailer);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewTrailer);
            imageViewShare = (ImageView) itemView.findViewById(R.id.imageViewShare);
        }
    }
}
