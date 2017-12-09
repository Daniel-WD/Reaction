package com.danielweidensdoerfer.reaction.game.generator;

public final class GeneratorResult {

    public final Item[][] field;
    public final Object[] targets;
    public final long time;

    public GeneratorResult(Item[][] field, Object[] targets, long time) {
        this.field = field;
        this.targets = targets;
        this.time = time;
    }
}
