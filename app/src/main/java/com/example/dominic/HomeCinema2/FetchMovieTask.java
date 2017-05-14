package com.example.dominic.HomeCinema2;


import android.net.Uri;
import android.os.AsyncTask;

import org.jetbrains.annotations.Contract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.example.dominic.HomeCinema2.callbacks.MyCallBack;
import com.example.dominic.HomeCinema2.movie_model.Movies;

/**
 * Created by Dominic Idagu on 5/13/2017.
 */

public class FetchMovieTask extends AsyncTask<String,Void,Movies[]> {

    private final MyCallBack movieTaskCallback;

    public FetchMovieTask(MyCallBack movieTaskCallback) {
        this.movieTaskCallback = movieTaskCallback;
    }

    @Override
    protected Movies[] doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        final String BASE_URL = "https://api.themoviedb.org/3/movie/";
        final String API_KEY = "api_key";

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(params[0])
                .appendQueryParameter(API_KEY, "7a946defea5c0d5eb3c0dfbcea204e7f")
                .build();

        String jsonString = NetworkRequest.getJsonString(uri);

        try {
            return getMoviesFromJson(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Contract("null -> null")
    private Movies[] getMoviesFromJson(String movieJsonString) throws JSONException {
        final String ORIGINAL_TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String VOTE_AVERAGE = "vote_average";
        final String RELEASE_DATE = "release_date";
        final String MOVIE_ID = "id";
        final String RESULT_ARRAY = "results";

        if (movieJsonString == null || "".equals(movieJsonString)) {
            return null;
        }

        JSONObject jsonObjectMovie = new JSONObject(movieJsonString);
        JSONArray jsonArrayMovies = jsonObjectMovie.getJSONArray(RESULT_ARRAY);

        Movies[] movies = new Movies[jsonArrayMovies.length()];

        for (int i = 0; i < jsonArrayMovies.length(); i++) {
            JSONObject object = jsonArrayMovies.getJSONObject(i);
            movies[i] = new Movies(
                    object.getInt(MOVIE_ID),
                    object.getString(ORIGINAL_TITLE),
                    object.getString(POSTER_PATH),
                    object.getString(OVERVIEW),
                    object.getString(VOTE_AVERAGE),
                    object.getString(RELEASE_DATE),
                    null);
        }
        return movies;
    }

    @Override
    protected void onPostExecute(Movies[] movies) {
        if (movies != null) {
            movieTaskCallback.updateAdapter(movies);
        }
    }
}
