package com.danielweidensdoerfer.reaction.utils;

import android.view.View;

public class ViewUtils {

    public static void visibility(int visibility, View... views) {
        if(views == null || views.length == 0 ||
                (visibility != View.INVISIBLE && visibility != View.VISIBLE
                        && visibility != View.GONE)) return;
        for (View view : views) {
            view.setVisibility(visibility);
        }
    }

}
