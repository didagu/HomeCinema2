package com.example.dominic.HomeCinema2.movie_model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dominic Idagu on 5/13/2017.
 */

public class Trailer implements Parcelable {

    private final String trailerId;
    private final String key;
    private final String name;

    public Trailer(String trailerId, String key, String name) {
        this.trailerId = trailerId;
        this.key = key;
        this.name = name;
    }

    public Trailer(Parcel in) {
        trailerId = in.readString();
        key = in.readString();
        name = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(trailerId);
        parcel.writeString(key);
        parcel.writeString(name);
    }
}
