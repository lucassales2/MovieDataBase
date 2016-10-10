package com.moviedatabase.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moviedatabase.R;
import com.moviedatabase.networking.movies.dto.MovieDto;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 27/09/16.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private static final String HTTP_IMAGE_TMDB_ORG_T_P_W185_S = "http://image.tmdb.org/t/p/w185/%s";
    private ArrayList<MovieDto> movieDtoList = new ArrayList<>();
    private WeakReference<MoviesAdapterListener> listener;

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
        Glide.with(holder.imageView.getContext()).load(String.format(HTTP_IMAGE_TMDB_ORG_T_P_W185_S, movieDto.getPoster_path())).into(holder.imageView);
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

    public void addAll(List<MovieDto> movieDtos) {
        int size = movieDtoList.size();
        movieDtoList.addAll(movieDtos);
        notifyItemRangeChanged(size, movieDtos.size());
    }

    public ArrayList<MovieDto> getMovies() {
        return movieDtoList;
    }

    public void clear() {
        movieDtoList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
        }
    }
}
