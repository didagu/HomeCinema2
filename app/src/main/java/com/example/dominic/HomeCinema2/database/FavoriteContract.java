package com.example.dominic.HomeCinema2.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Dominic Idagu on 5/13/2017.
 */

public class FavoriteContract {

    public static final String CONTENT_AUTHORITY = "com.example.dominic.HomeCinema2.database";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class FavoriteEntry implements BaseColumns {
        public static final String TABLE_FAVORITE = "favorite_movie";
        public static final String _ID = "_id";
        public static final String MOVIE_ID = "movie_id";
        public static final String TITLE = "title";
        public static final String POSTER_PATH = "poster_path";
        public static final String OVERVIEW = "overview";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String RELEASE_DATE = "release_date";
        public static final String MOVIE_RUNTIME = "movie_runtime";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_FAVORITE).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY +
                        "/" + TABLE_FAVORITE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY +
                        "/" + TABLE_FAVORITE;

        public static Uri buildFavoriteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
