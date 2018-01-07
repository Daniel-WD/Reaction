package com.danielweidensdoerfer.reaction.game.generator;

import com.danielweidensdoerfer.reaction.bases.itembase.Item;

public final class GeneratorResult {

    public final int points;
    public final Item[][] field;
    public final Target[] targets;
    public final long time;

    public GeneratorResult(Item[][] field, Target[] targets, long time, int points) {
        this.points = points;
        this.field = field;
        this.targets = targets;
        this.time = time;
    }
}
