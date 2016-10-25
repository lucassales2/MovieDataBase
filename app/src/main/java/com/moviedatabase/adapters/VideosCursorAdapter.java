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

    private WeakReference<VideosAdapterListener> listener;

    public VideosCursorAdapter(Cursor cursor, VideosAdapterListener videosAdapterListener) {
        super(cursor);
        listener = new WeakReference<>(videosAdapterListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        holder.textView.setText(cursor.getString(MovieDetailFragment.INDEX_VIDEO_COL_NAME));
        final String key = cursor.getString(MovieDetailFragment.INDEX_VIDEO_COL_KEY);
        Glide.with(holder.itemView.getContext()).load(String.format("http://img.youtube.com/vi/%s/mqdefault.jpg", key)).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.get().onVideoClick(key);
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

        private ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textViewTrailer);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewTrailer);
        }
    }
}
