package com.moviedatabase.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moviedatabase.R;
import com.moviedatabase.networking.movies.dto.ReviewDto;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 17/10/16.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private ArrayList<ReviewDto> reviewList;
    private WeakReference<ReviewsAdapterListener> listener;

    public ReviewsAdapter(ReviewsAdapterListener listener) {
        this.listener = new WeakReference<>(listener);
        reviewList = new ArrayList<>();
    }

    public ReviewsAdapter(List<ReviewDto> reviewList, ReviewsAdapterListener listener) {
        this.listener = new WeakReference<>(listener);
        this.reviewList = new ArrayList<>(reviewList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ReviewDto reviewDto = reviewList.get(position);
        holder.textViewContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.get().onReviewClick(reviewDto.getUrl());
            }
        });
        holder.textViewContent.setText(reviewDto.getContent());
        holder.textViewAuthor.setText(reviewDto.getAuthor());
    }

    public void add(ReviewDto reviewDto) {
        reviewList.add(reviewDto);
        notifyItemInserted(reviewList.size() - 1);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
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
