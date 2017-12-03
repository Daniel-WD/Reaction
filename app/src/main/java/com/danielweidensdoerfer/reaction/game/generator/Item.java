package com.danielweidensdoerfer.reaction.game.generator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;

import com.danielweidensdoerfer.reaction.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Item {

    public final long id;
    public final Drawable drawable;
    public int color = Color.WHITE;
    final ArrayList<Integer> tags;

    Item(long id, Context context, int drawableId, Integer... ts) {
        this.id = id;
        drawable = context.getResources().getDrawable(drawableId, context.getTheme());
        tags = new ArrayList<>();
        if(ts != null && ts.length != 0) {
            tags.addAll(Arrays.asList(ts));
        }
    }

    Item(long id, Drawable d, ArrayList<Integer> ts) {
        this.id = id;
        drawable = d;
        tags = new ArrayList<>(ts);
    }

    Item copy() {
        return new Item(id, drawable, tags);
    }

}
