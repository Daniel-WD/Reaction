package com.danielweidensdoerfer.reaction.bases;

import android.support.v4.content.ContextCompat;

import com.danielweidensdoerfer.reaction.R;
import com.danielweidensdoerfer.reaction.ReactionActivity;

public class Colorbase {

    public static int[][] colors;

    public static void init(ReactionActivity act) {
        colors = new int[][] {
                {
                        ContextCompat.getColor(act, R.color._11),
                        ContextCompat.getColor(act, R.color._12),
                        ContextCompat.getColor(act, R.color._13),
                        ContextCompat.getColor(act, R.color._14),
                        ContextCompat.getColor(act, R.color._15)
                },
                {
                        ContextCompat.getColor(act, R.color._21),
                        ContextCompat.getColor(act, R.color._22),
                        ContextCompat.getColor(act, R.color._23),
                        ContextCompat.getColor(act, R.color._24),
                        ContextCompat.getColor(act, R.color._25)
                },
                {
                        ContextCompat.getColor(act, R.color._31),
                        ContextCompat.getColor(act, R.color._32),
                        ContextCompat.getColor(act, R.color._33),
                        ContextCompat.getColor(act, R.color._34),
                        ContextCompat.getColor(act, R.color._35)
                },
                {
                        ContextCompat.getColor(act, R.color._41),
                        ContextCompat.getColor(act, R.color._42),
                        ContextCompat.getColor(act, R.color._43),
                        ContextCompat.getColor(act, R.color._44),
                        ContextCompat.getColor(act, R.color._45)
                },
                {
                        ContextCompat.getColor(act, R.color._51),
                        ContextCompat.getColor(act, R.color._52),
                        ContextCompat.getColor(act, R.color._53),
                        ContextCompat.getColor(act, R.color._54),
                        ContextCompat.getColor(act, R.color._55)
                },
                {
                        ContextCompat.getColor(act, R.color._61),
                        ContextCompat.getColor(act, R.color._62),
                        ContextCompat.getColor(act, R.color._63),
                        ContextCompat.getColor(act, R.color._64),
                        ContextCompat.getColor(act, R.color._65)
                },
                {
                        ContextCompat.getColor(act, R.color._71),
                        ContextCompat.getColor(act, R.color._72),
                        ContextCompat.getColor(act, R.color._73),
                        ContextCompat.getColor(act, R.color._74),
                        ContextCompat.getColor(act, R.color._75)
                },
                {
                        ContextCompat.getColor(act, R.color._81),
                        ContextCompat.getColor(act, R.color._82),
                        ContextCompat.getColor(act, R.color._83),
                        ContextCompat.getColor(act, R.color._84),
                        ContextCompat.getColor(act, R.color._85)
                }
        };

    }

}
