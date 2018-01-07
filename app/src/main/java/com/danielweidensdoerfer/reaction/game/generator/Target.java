package com.danielweidensdoerfer.reaction.game.generator;

import com.danielweidensdoerfer.reaction.bases.itembase.Item;

public abstract class Target {

    static class ColorTarget extends Target{
        final int color;

        ColorTarget(int color) {
            this.color = color;
        }

        @Override
        public boolean isTarget(Item item) {
            return this.color == item.color;
        }

        @Override
        public Item item() {
            return null;
        }
    }

    static class ItemTarget extends Target {
        final Item item;

        ItemTarget(Item item) {
            this.item = item;
        }

        @Override
        public boolean isTarget(Item item) {
            return this.item.id == item.id;
        }

        @Override
        public Item item() {
            return item;
        }
    }

    public abstract boolean isTarget(Item item);
    public abstract Item item();

}
