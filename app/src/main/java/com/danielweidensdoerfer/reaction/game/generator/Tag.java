package com.danielweidensdoerfer.reaction.game.generator;

abstract class Tag {

    static final int FRUIT = 0;
    static final int CURRENCY = 1;
    static final int NUMBER = 2;
    static final int EMOJI = 3;
    static final int WEATHER = 4;

    static final int[] tags = {
            FRUIT,
            CURRENCY,
            NUMBER,
            EMOJI,
            WEATHER
    };

}
