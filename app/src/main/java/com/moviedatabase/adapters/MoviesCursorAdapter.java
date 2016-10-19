package com.moviedatabase.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moviedatabase.MovieListFragment;
import com.moviedatabase.R;
import com.moviedatabase.Utility;
import com.moviedatabase.ui.CursorRecyclerViewAdapter;

import java.lang.ref.WeakReference;

/**
 * Created by lucas on 19/10/16.
 */

public class MoviesCursorAdapter extends CursorRecyclerViewAdapter<MoviesCursorAdapter.ViewHolder> {

    private WeakReference<MoviesCursorAdapterListener> listener;

    public MoviesCursorAdapter(Cursor cursor, MoviesCursorAdapterListener listener) {
        super(cursor);
        this.listener = new WeakReference<>(listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        final int id = cursor.getInt(MovieListFragment.COL_ID);
        Glide.with(holder.imageView.getContext()).load(Utility.getFullPosterPath(cursor.getString(MovieListFragment.COL_POSTER_PATH))).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.get().onItemClick(id);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_layout, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        private ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
        }
    }
}
