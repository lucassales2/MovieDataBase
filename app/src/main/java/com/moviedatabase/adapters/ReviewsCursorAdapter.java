package com.moviedatabase.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moviedatabase.MovieDetailFragment;
import com.moviedatabase.R;
import com.moviedatabase.ui.CursorRecyclerViewAdapter;

import java.lang.ref.WeakReference;

/**
 * Created by lucas on 24/10/16.
 */

public class ReviewsCursorAdapter extends CursorRecyclerViewAdapter<ReviewsCursorAdapter.ViewHolder> {
    private WeakReference<ReviewsCursorAdapterListener> listener;

    public ReviewsCursorAdapter(Cursor cursor, ReviewsCursorAdapterListener reviewsCursorAdapterListener) {
        super(cursor);
        listener = new WeakReference<>(reviewsCursorAdapterListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        final String url = cursor.getString(MovieDetailFragment.INDEX_REVIEW_COL_URL);
        holder.textViewContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.get().onReviewClick(url);
            }
        });
        holder.textViewContent.setText(cursor.getString(MovieDetailFragment.INDEX_REVIEW_COL_CONTENT));
        holder.textViewAuthor.setText(cursor.getString(MovieDetailFragment.INDEX_REVIEW_COL_AUTHOR));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewAuthor;
        final TextView textViewContent;

        private ViewHolder(View itemView) {
            super(itemView);
            textViewAuthor = (TextView) itemView.findViewById(R.id.textViewReviewAuthor);
            textViewContent = (TextView) itemView.findViewById(R.id.textViewReviewContent);
        }
    }
}
