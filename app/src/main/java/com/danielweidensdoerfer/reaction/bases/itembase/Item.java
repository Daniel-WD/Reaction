package com.danielweidensdoerfer.reaction.bases.itembase;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class Item {

    public final long id;
    public final Drawable drawable;
    public int color = Color.WHITE;

    public Item(long id, Context context, int drawableId) {
        this.id = id;
        drawable = context.getResources().getDrawable(drawableId, context.getTheme());
    }

    Item(long id, Drawable d, int c) {
        this.id = id;
        drawable = d;
        color = c;
    }

    public Item copy() {
        return new Item(id, drawable, color);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Item)) return false;
        Item item = (Item) obj;
        return item.id == this.id;
    }
}
