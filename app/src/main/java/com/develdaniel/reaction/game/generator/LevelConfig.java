package com.develdaniel.reaction.game.generator;

class LevelConfig {

    final int winPoints;
    final int numRounds;
    final int rows, cols;
    final int targets;
    final long startTime, endTime;

    private float fraction = 0;

    LevelConfig(int points, int rounds, int rs, int cs, long sTime, long eTime, int targets) {
        winPoints = points;
        numRounds = rounds;
        rows = rs;
        cols = cs;
        startTime = sTime;
        endTime = eTime;
        this.targets = targets;
    }

    void setCurrentRound(int round) {
        if(round < 1 || round > numRounds) return;
        fraction = (float)(round-1)/(float)(numRounds-1);
    }

    long time() {
        return startTime + (long)(fraction*(float)(endTime-startTime));
    }

}
