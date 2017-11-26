package com.danielweidensdoerfer.reaction.game;

import android.content.Context;
import android.graphics.drawable.VectorDrawable;
import android.support.v4.content.ContextCompat;

import com.danielweidensdoerfer.reaction.R;

import java.util.ArrayList;
import java.util.Random;

public class GridGenerator {

    private static int[] sColors;

    //half currency
    private static Item sHalfDollar;
    private static Item sHalfDoublePound;
    private static Item sHalfEuro;
    private static Item sHalfP;
    private static Item sHalfPound;
    private static Item sHalfRp;
    private static Item sHalfWon;
    private static Item sHalfYen;

    //currency
    private static Item sDollar;
    private static Item sDoublePound;
    private static Item sEuro;
    private static Item sP;
    private static Item sPound;
    private static Item sRp;
    private static Item sWon;
    private static Item sYen;

    //numbers/symbols
    private static Item sOne;
    private static Item sTwo;
    private static Item sThree;
    private static Item sFour;
    private static Item sFive;
    private static Item sSix;
    private static Item sSeven;
    private static Item sEight;
    private static Item sNine;
    private static Item sZero;

    //fruits
    private static Item sBanana;
    private static Item sApple;
    private static Item sMelon;
    private static Item sPear;

    //objects
    private static Item sCircleFilled;
    private static Item sCircleOutline;
    private static Item sSquareFilled;
    private static Item sSquareOutline;
    private static Item sSquareRoundFilled;
    private static Item sSquareRoundOutline;

    private static ArrayList<Item> sItems = new ArrayList<>();

    public static void init(Context context) {

        sColors = new int[] {
//                ContextCompat.getColor(context, R.color._1),
                ContextCompat.getColor(context, R.color._2),
                ContextCompat.getColor(context, R.color._3),
                ContextCompat.getColor(context, R.color._4),
                ContextCompat.getColor(context, R.color._5),
                ContextCompat.getColor(context, R.color._6),
                ContextCompat.getColor(context, R.color._7),
//                ContextCompat.getColor(context, R.color._8),
                ContextCompat.getColor(context, R.color._9),
                ContextCompat.getColor(context, R.color._10),
                ContextCompat.getColor(context, R.color._11),
//                ContextCompat.getColor(context, R.color._12),
                ContextCompat.getColor(context, R.color._13),
                ContextCompat.getColor(context, R.color._14),
                ContextCompat.getColor(context, R.color._15),
                ContextCompat.getColor(context, R.color._16),
//                ContextCompat.getColor(context, R.color._17),
//                ContextCompat.getColor(context, R.color._18),
//                ContextCompat.getColor(context, R.color._19),
//                ContextCompat.getColor(context, R.color._20)
        };

        sHalfEuro = new Item(context.getResources().getDrawable(R.drawable.img_half_cur_euro, context.getTheme()));

        sItems.add(sHalfEuro);

        sHalfDollar = new Item(context.getResources().getDrawable(R.drawable.img_half_cur_dollar, context.getTheme()));

        sItems.add(sHalfDollar);

        sHalfDoublePound = new Item(context.getResources().getDrawable(R.drawable.img_half_cur_double_pound, context.getTheme()));

        sItems.add(sHalfDoublePound);

        sHalfP = new Item(context.getResources().getDrawable(R.drawable.img_half_cur_p, context.getTheme()));

        sItems.add(sHalfP);

        sHalfPound = new Item(context.getResources().getDrawable(R.drawable.img_half_cur_pound, context.getTheme()));

        sItems.add(sHalfPound);

        sHalfRp = new Item(context.getResources().getDrawable(R.drawable.img_half_cur_rp, context.getTheme()));

        sItems.add(sHalfRp);

        sHalfWon = new Item(context.getResources().getDrawable(R.drawable.img_half_cur_won, context.getTheme()));

        sItems.add(sHalfWon);

        sHalfYen = new Item(context.getResources().getDrawable(R.drawable.img_half_cur_yen, context.getTheme()));

        sItems.add(sHalfYen);

        /*sEuro = new Item(context.getResources().getDrawable(R.drawable.img_cur_euro, context.getTheme()));

        sItems.add(sEuro);

        sDollar = new Item(context.getResources().getDrawable(R.drawable.img_cur_dollar, context.getTheme()));

        sItems.add(sDollar);

        sDoublePound = new Item(context.getResources().getDrawable(R.drawable.img_cur_double_pound, context.getTheme()));

        sItems.add(sDoublePound);

        sP = new Item(context.getResources().getDrawable(R.drawable.img_cur_p, context.getTheme()));

        sItems.add(sP);

        sPound = new Item(context.getResources().getDrawable(R.drawable.img_cur_pound, context.getTheme()));

        sItems.add(sPound);

        sRp = new Item(context.getResources().getDrawable(R.drawable.img_cur_rp, context.getTheme()));

        sItems.add(sRp);

        sWon = new Item(context.getResources().getDrawable(R.drawable.img_cur_won, context.getTheme()));

        sItems.add(sWon);

        sYen = new Item(context.getResources().getDrawable(R.drawable.img_cur_yen, context.getTheme()));

        sItems.add(sYen);*/
/*
        sOne = new Item(context.getResources().getDrawable(R.drawable.img_num_1, context.getTheme()));

        sItems.add(sOne);

        sTwo = new Item(context.getResources().getDrawable(R.drawable.img_num_2, context.getTheme()));

        sItems.add(sTwo);

        sThree = new Item(context.getResources().getDrawable(R.drawable.img_num_3, context.getTheme()));

        sItems.add(sThree);

        sFour = new Item(context.getResources().getDrawable(R.drawable.img_num_4, context.getTheme()));

        sItems.add(sFour);

        sFive = new Item(context.getResources().getDrawable(R.drawable.img_num_5, context.getTheme()));

        sItems.add(sFive);

        sSix = new Item(context.getResources().getDrawable(R.drawable.img_num_6, context.getTheme()));

        sItems.add(sSix);

        sSeven = new Item(context.getResources().getDrawable(R.drawable.img_num_7, context.getTheme()));

        sItems.add(sSeven);

        sEight = new Item(context.getResources().getDrawable(R.drawable.img_num_8, context.getTheme()));

        sItems.add(sEight);

        sNine = new Item(context.getResources().getDrawable(R.drawable.img_num_9, context.getTheme()));

        sItems.add(sNine);

        sZero = new Item(context.getResources().getDrawable(R.drawable.ic_zero, context.getTheme()));

        sItems.add(sZero);*/

        /*sBanana = new Item(context.getResources().getDrawable(R.drawable.img_fr_banana, context.getTheme()));

        sItems.add(sBanana);

        sApple = new Item(context.getResources().getDrawable(R.drawable.img_fr_apple, context.getTheme()));

        sItems.add(sApple);

        sMelon = new Item(context.getResources().getDrawable(R.drawable.img_fr_melon, context.getTheme()));

        sItems.add(sMelon);

        sPear = new Item(context.getResources().getDrawable(R.drawable.img_fr_pear, context.getTheme()));

        sItems.add(sPear);*/
//
//        sCircleFilled = new Item(context.getResources().getDrawable(R.drawable.img_obj_circle_filled, context.getTheme()));
//
//        sItems.add(sCircleFilled);
//
//        sCircleOutline = new Item(context.getResources().getDrawable(R.drawable.img_obj_circle_outline, context.getTheme()));
//
//        sItems.add(sCircleOutline);
//
//        sSquareFilled = new Item(context.getResources().getDrawable(R.drawable.img_obj_square_filled, context.getTheme()));
//
//        sItems.add(sSquareFilled);
//
//        sSquareOutline = new Item(context.getResources().getDrawable(R.drawable.img_obj_square_outline, context.getTheme()));
//
//        sItems.add(sSquareOutline);
//
//        sSquareRoundFilled = new Item(context.getResources().getDrawable(R.drawable.img_obj_square_round_filled, context.getTheme()));
//
//        sItems.add(sSquareRoundFilled);
//
//        sSquareRoundOutline = new Item(context.getResources().getDrawable(R.drawable.img_obj_square_round_outline, context.getTheme()));
//
//        sItems.add(sSquareRoundOutline);

        Random random = new Random();
        for (Item item : sItems) {
            item.drawable.setTint(sColors[random.nextInt(sColors.length)]);
        }
    }

    public static Item[][] generate(int rows, int cols) {
        Item[][] result = new Item[cols][rows];
        Random r = new Random();
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                result[i][j] = sItems.get(r.nextInt(sItems.size()));
            }
        }
        return result;
    }


}
