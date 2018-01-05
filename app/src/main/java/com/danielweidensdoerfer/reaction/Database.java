package com.danielweidensdoerfer.reaction;

import android.content.Context;
import android.content.SharedPreferences;

public class Database {

    private static final String KEY_REC_POINTS = "key_rec_points";
    private static final String KEY_PLAYED_ROUNDS = "key_played_rounds";
    private static final String KEY_ROUND_RECORD = "key_round_record";
    private static final String KEY_REMOVED_OBJECTS = "key_removed_objects";

    public static int recordPoints = 0;
    public static int playedRounds = 0;
    public static int roundRecord = 0;
    public static int removedObjects = 0;

    private static SharedPreferences mPreferences;

    public static void init(ReactionActivity act) {
        mPreferences = act.getPreferences(Context.MODE_PRIVATE);
    }

    public static void load() {
        recordPoints = mPreferences.getInt(KEY_REC_POINTS, 0);
        playedRounds = mPreferences.getInt(KEY_PLAYED_ROUNDS, 0);
        roundRecord = mPreferences.getInt(KEY_ROUND_RECORD, 0);
        removedObjects = mPreferences.getInt(KEY_REMOVED_OBJECTS, 0);
    }

    public static void save() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(KEY_REC_POINTS, recordPoints);
        editor.putInt(KEY_PLAYED_ROUNDS, playedRounds);
        editor.putInt(KEY_ROUND_RECORD, roundRecord);
        editor.putInt(KEY_REMOVED_OBJECTS, removedObjects);
        editor.apply();
    }

}
