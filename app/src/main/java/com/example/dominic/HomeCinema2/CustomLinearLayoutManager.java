package com.example.dominic.HomeCinema2;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Dominic Idagu on 5/13/2017.
 */

public class CustomLinearLayoutManager extends LinearLayoutManager {

    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
