package com.danielweidensdoerfer.reaction.game.generator;

class LevelConfig {

    final int winPoints;
    final int numRounds;
    final int rows, cols;
    private final long startTime, endTime;
    private final int startTargets, endTargets;

    private float fraction = 0;

    LevelConfig(int points, int rounds, int rs, int cs, long sTime, long eTime, int sTargets, int eTargets) {
        winPoints = points;
        numRounds = rounds;
        rows = rs;
        cols = cs;
        startTime = sTime;
        endTime = eTime;
        startTargets = sTargets;
        endTargets = eTargets;
    }

    void setCurrentRound(int round) {
        if(round < 1 || round > numRounds) return;
        fraction = (float)(round-1)/(float)(numRounds-1);
    }

    long time() {
        return startTime + (long)(fraction*(float)(endTime-startTime));
    }

    int targets() {
        return startTargets + (int) (fraction*(float)(endTargets-startTargets));
    }

}
