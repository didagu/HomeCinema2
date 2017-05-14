package com.example.dominic.HomeCinema2;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Collections;

import com.example.dominic.HomeCinema2.callbacks.MyCallBack;
import com.example.dominic.HomeCinema2.database.FavoriteContract;
import com.example.dominic.HomeCinema2.movie_model.Movies;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment {

    private static final String KEY_MOVIE_LIST = "movie_list";
    private static final String KEY_SELECTED_POSITION = "SELECTED_POSITION";
    private static final String SORT_ORDER = "sort_order";
    private int itemPosition = GridView.INVALID_POSITION;
    private GridView gridViewMovie;
    private MovieAdapter movieAdapter;
    private ArrayList<Movies> movieList;
    private SharedPreferences preferences;

    public interface Callback {
        void onItemSelected(Movies movie);
    }



    public MainActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_MOVIE_LIST)) {
            movieList = new ArrayList<>();
        } else {
            movieList = savedInstanceState.getParcelableArrayList(KEY_MOVIE_LIST);
        }
        setHasOptionsMenu(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_activity, container, false);
        movieAdapter = new MovieAdapter(getActivity(), movieList);
        gridViewMovie = (GridView) view.findViewById(R.id.grid_view);
        // Inflate the layout for this fragment
        gridViewMovie.setAdapter(movieAdapter);
        gridViewMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movies movie = movieAdapter.getItem(position);
                ((Callback) getActivity()).onItemSelected(movie);
                itemPosition = position;
            }
        });



        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_SELECTED_POSITION)) {
            itemPosition = savedInstanceState.getInt(KEY_SELECTED_POSITION);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovie();
    }

    private void updateMovie() {

            String sortingOrder = preferences.getString(SORT_ORDER,
                    getString(R.string.pref_sort_popular_value));

            if (!sortingOrder.equals(getString(R.string.pref_sort_favorite_value))) {
                loadMovie(sortingOrder);
            } else {
                fetcFavouriteMovies();
            }

    }

    private void loadMovie(String sortingOrder) {
        FetchMovieTask moviesTask = new FetchMovieTask(new MyCallBack() {
            @Override
            public void updateAdapter(Movies[] movies) {
                if (movies != null) {
                    movieAdapter.clear();
                    Collections.addAll(movieList, movies);
                    movieAdapter.notifyDataSetChanged();
                    if (itemPosition != GridView.INVALID_POSITION) {
                        gridViewMovie.smoothScrollToPosition(itemPosition);
                    }
                }
            }
        });
        moviesTask.execute(sortingOrder);
    }

    private void fetcFavouriteMovies() {
        Cursor cursor = getActivity().getContentResolver()
                .query(FavoriteContract.FavoriteEntry.CONTENT_URI, null, null, null, null);

        movieAdapter.clear();
        assert cursor != null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Movies movie = new Movies(
                        cursor.getInt(cursor.getColumnIndex(
                                FavoriteContract.FavoriteEntry.MOVIE_ID)),
                        cursor.getString(cursor.getColumnIndex(
                                FavoriteContract.FavoriteEntry.TITLE)),
                        cursor.getString(cursor.getColumnIndex(
                                FavoriteContract.FavoriteEntry.POSTER_PATH)),
                        cursor.getString(cursor.getColumnIndex(
                                FavoriteContract.FavoriteEntry.OVERVIEW)),
                        cursor.getString(cursor.getColumnIndex(
                                FavoriteContract.FavoriteEntry.VOTE_AVERAGE)),
                        cursor.getString(cursor.getColumnIndex(
                                FavoriteContract.FavoriteEntry.RELEASE_DATE)),
                        cursor.getString(cursor.getColumnIndex(
                                FavoriteContract.FavoriteEntry.MOVIE_RUNTIME)));
                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        movieAdapter.notifyDataSetChanged();
        if (itemPosition != GridView.INVALID_POSITION) {
            gridViewMovie.smoothScrollToPosition(itemPosition);
        }
        cursor.close();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_MOVIE_LIST, movieList);
        if (itemPosition != GridView.INVALID_POSITION) {
            outState.putInt(KEY_SELECTED_POSITION, itemPosition);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (id == R.id.action_most_popular) {
            setNewSortOrder(R.string.pref_sort_popular_value);
            updateMovie();
            return true;
        } else if (id == R.id.action_top_rating) {
            setNewSortOrder(R.string.pref_sort_rating_value);
            updateMovie();
            return true;
        } else if (id == R.id.action_favorite) {
            setNewSortOrder(R.string.pref_sort_favorite_value);
            updateMovie();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNewSortOrder(int sortOrder) {
        String orderKey = getResources().getString(sortOrder);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SORT_ORDER, orderKey);
        editor.apply();
    }

}
