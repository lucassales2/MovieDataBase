package com.moviedatabase;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moviedatabase.adapters.ReviewsCursorAdapter;
import com.moviedatabase.adapters.ReviewsCursorAdapterListener;
import com.moviedatabase.adapters.VideosCursorAdapter;
import com.moviedatabase.adapters.VideosCursorAdapterListener;
import com.moviedatabase.data.MovieContract.MovieEntry;
import com.moviedatabase.data.MovieContract.ReviewEntry;
import com.moviedatabase.data.MovieContract.VideoEntry;
import com.moviedatabase.sync.MovieSyncAdapter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by lucas on 10/10/16.
 */

public class MovieDetailFragment extends Fragment implements VideosCursorAdapterListener, ReviewsCursorAdapterListener {
    public static final String TAG = MovieListFragment.class.getSimpleName();

    public static final int INDEX_VIDEO_COL_KEY = 0;
    public static final int INDEX_VIDEO_COL_NAME = 1;

    public static final int INDEX_REVIEW_COL_AUTHOR = 0;
    public static final int INDEX_REVIEW_COL_CONTENT = 1;
    public static final int INDEX_REVIEW_COL_URL = 2;

    private static final String MOVIE_ID = "movie";

    private static final String[] MOVIE_COLUMNS = new String[]{
            MovieEntry.COLUMN_TITLE,
            MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieEntry.COLUMN_RELEASE_DATE,
            MovieEntry.COLUMN_RUNTIME,
            MovieEntry.COLUMN_RATING,
            MovieEntry.COLUMN_OVERVIEW,
            MovieEntry.COLUMN_POSTER_PATH,
            MovieEntry.COLUMN_FAVORITED,
    };

    private static final String[] VIDEO_COLUMNS = new String[]{
            VideoEntry.COLUMN_KEY,
            VideoEntry.COLUMN_NAME,
            VideoEntry._ID
    };

    private static final String[] REVIEW_COLUMNS = new String[]{
            ReviewEntry.COLUMN_AUTHOR,
            ReviewEntry.COLUMN_CONTENT,
            ReviewEntry.COLUMN_URL,
            ReviewEntry._ID
    };

    private static final int MOVIE_LOADER_ID = 1;
    private static final int VIDEO_LOADER_ID = 2;
    private static final int REVIEW_LOADER_ID = 3;
    private static final int FAVORITED = 1597;

    private VideosCursorAdapter videosCursorAdapter;
    private ReviewsCursorAdapter reviewsAdapter;
    private RecyclerView recyclerViewVideos;
    private RecyclerView recyclerViewReviews;
    private TextView textViewReview;
    private TextView textViewTrailer;
    private TextView textViewTitle;
    private TextView textViewOriginalTitle;
    private TextView textViewYear;
    private TextView textViewRuntime;
    private TextView textViewRating;
    private TextView textViewOverview;
    private FloatingActionButton fab;

    private ImageView imageViewPoster;

    private View dividerReviews;
    private View dividerTrailers;
    private boolean isFavorite = false;

    public static MovieDetailFragment newInstance(long movieId) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putLong(MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args == null || !args.containsKey(MOVIE_ID) || args.getLong(MOVIE_ID) == -1L) {
            return;
        }
        if (savedInstanceState == null) {
            MovieSyncAdapter.syncImmediatelyWithMovieId(getContext(), args.getLong(MOVIE_ID));
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        Bundle args = getArguments();
        if (args != null && args.containsKey(MOVIE_ID) && args.getLong(MOVIE_ID) != -1L) {
            getLoaderManager().initLoader(MOVIE_LOADER_ID, args, new MovieLoader());
            getLoaderManager().initLoader(VIDEO_LOADER_ID, args, new VideoLoader());
            getLoaderManager().initLoader(REVIEW_LOADER_ID, args, new ReviewLoader());
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isFavorite = !isFavorite;
                    ContentValues values = new ContentValues();
                    values.put(MovieEntry.COLUMN_FAVORITED, isFavorite ? 1 : 0);
                    getContext().getContentResolver().update(
                            MovieEntry.CONTENT_URI, values,
                            MovieEntry._ID + "=?",
                            new String[]{String.valueOf(getArguments().getLong(MOVIE_ID))});
                }
            });
        }


        textViewTitle = (TextView) rootView.findViewById(R.id.detail_title);
        textViewOriginalTitle = (TextView) rootView.findViewById(R.id.detail_original_title);
        textViewYear = (TextView) rootView.findViewById(R.id.detail_year_textview);
        textViewRuntime = (TextView) rootView.findViewById(R.id.detail_runtime_textview);
        textViewRating = (TextView) rootView.findViewById(R.id.detail_rating_textview);
        textViewOverview = (TextView) rootView.findViewById(R.id.detail_overview);
        imageViewPoster = (ImageView) rootView.findViewById(R.id.detail_poster);
        textViewReview = (TextView) rootView.findViewById(R.id.textViewReview);
        textViewTrailer = (TextView) rootView.findViewById(R.id.textViewTrailer);

        recyclerViewVideos = (RecyclerView) rootView.findViewById(R.id.recyclerView_videos);
        recyclerViewReviews = (RecyclerView) rootView.findViewById(R.id.recyclerView_reviews);

        dividerReviews = rootView.findViewById(R.id.divider_reviews);
        dividerTrailers = rootView.findViewById(R.id.divider_trailers);

        recyclerViewReviews.setNestedScrollingEnabled(false);
        recyclerViewVideos.setNestedScrollingEnabled(false);
        recyclerViewVideos.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(getContext()));

        videosCursorAdapter = new VideosCursorAdapter(null, this);
        reviewsAdapter = new ReviewsCursorAdapter(null, this);

        recyclerViewVideos.setAdapter(videosCursorAdapter);
        recyclerViewReviews.setAdapter(reviewsAdapter);
        return rootView;
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putParcelable(MOVIE_VM_KEY, viewModel);
//        super.onSaveInstanceState(outState);
//    }

    @Override
    public void onVideoClick(String key) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("http://www.youtube.com/watch?v=%s", key))));
    }

    @Override
    public void onShareClick(String key, String name) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, name);
        share.putExtra(Intent.EXTRA_TEXT, String.format("http://www.youtube.com/watch?v=%s", key));

        startActivity(Intent.createChooser(share, String.format(getString(R.string.share_a_link), name)));
    }

    @Override
    public void onReviewClick(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    private class MovieLoader implements LoaderManager.LoaderCallbacks<Cursor> {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            long movieId = args.getLong(MOVIE_ID);
            return new CursorLoader(
                    getContext(),
                    MovieEntry.buildMovieUri(movieId),
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (cursor.moveToFirst()) {
                String title = cursor.getString(0);
                String originalTitle = cursor.getString(1);
                Calendar instance = Calendar.getInstance();
                instance.setTime(new Date(cursor.getLong(2)));
                String runtime = String.format("%s min", cursor.getString(3));
                int i = instance.get(Calendar.YEAR);
                String year = String.valueOf(i);
                String rating = cursor.getString(4);
                String overview = cursor.getString(5);
                String fullPosterPath = Utility.getFullPosterPath(cursor.getString(6));
                isFavorite = cursor.getInt(7) == 1;
                textViewTitle.setText(title);
                if (!originalTitle.equals(title)) {
                    textViewOriginalTitle.setVisibility(View.VISIBLE);
                    textViewOriginalTitle.setText(originalTitle);
                }
                textViewYear.setText(year);
                textViewRuntime.setText(runtime);
                textViewRating.setText(rating);
                textViewOverview.setText(overview);
                Glide.with(getContext()).load(fullPosterPath).into(imageViewPoster);
                fab.setImageResource(isFavorite ? R.drawable.ic_favorite_black_24dp : R.drawable.ic_favorite_border_black_24dp);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    private class VideoLoader implements LoaderManager.LoaderCallbacks<Cursor> {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            long movieId = args.getLong(MOVIE_ID);
            return new CursorLoader(
                    getContext(),
                    VideoEntry.CONTENT_URI,
                    VIDEO_COLUMNS,
                    VideoEntry.COLUMN_MOVIE_ID + " =? AND " + VideoEntry.COLUMN_SITE + " =?",
                    new String[]{String.valueOf(movieId), getString(R.string.you_tube)},
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            videosCursorAdapter.swapCursor(cursor);
            if (cursor.moveToFirst()) {
                dividerTrailers.setVisibility(View.VISIBLE);
                textViewTrailer.setVisibility(View.VISIBLE);
                recyclerViewVideos.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            videosCursorAdapter.swapCursor(null);
        }
    }

    private class ReviewLoader implements LoaderManager.LoaderCallbacks<Cursor> {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            long movieId = args.getLong(MOVIE_ID);
            return new CursorLoader(
                    getContext(),
                    ReviewEntry.CONTENT_URI,
                    REVIEW_COLUMNS,
                    ReviewEntry.COLUMN_MOVIE_ID + " =?",
                    new String[]{String.valueOf(movieId)},
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            reviewsAdapter.swapCursor(cursor);
            if (cursor.moveToFirst()) {
                dividerReviews.setVisibility(View.VISIBLE);
                textViewReview.setVisibility(View.VISIBLE);
                recyclerViewReviews.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            reviewsAdapter.swapCursor(null);
        }
    }
}
