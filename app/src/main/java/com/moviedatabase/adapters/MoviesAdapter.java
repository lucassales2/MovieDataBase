package com.moviedatabase.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moviedatabase.R;
import com.moviedatabase.Utility;
import com.moviedatabase.networking.movies.dto.MovieDto;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by lucas on 27/09/16.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {


    private ArrayList<MovieDto> movieDtoList = new ArrayList<>();
    private WeakReference<MoviesAdapterListener> listener;
    private int page = 0;

    public MoviesAdapter(ArrayList<MovieDto> movieDtos, MoviesAdapterListener listener) {
        movieDtoList = movieDtos;
        this.listener = new WeakReference<>(listener);
        setHasStableIds(true);
    }

    public MoviesAdapter(MoviesAdapterListener listener) {
        movieDtoList = new ArrayList<>();
        this.listener = new WeakReference<>(listener);
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return movieDtoList.get(position).getId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MovieDto movieDto = movieDtoList.get(position);
        Glide.with(holder.imageView.getContext()).load(Utility.getFullPosterPath(movieDto.getPosterPath())).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.get().onItemClick(movieDto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieDtoList.size();
    }

    public void add(MovieDto movieDto) {
        movieDtoList.add(movieDto);
        notifyItemInserted(movieDtoList.size() -1);
    }

    public ArrayList<MovieDto> getMovies() {
        return movieDtoList;
    }

    public void clear() {
        movieDtoList.clear();
        notifyDataSetChanged();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        private ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
        }
    }
}
