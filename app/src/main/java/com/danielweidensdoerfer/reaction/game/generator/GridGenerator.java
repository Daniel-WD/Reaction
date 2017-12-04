package com.danielweidensdoerfer.reaction.game.generator;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.danielweidensdoerfer.reaction.R;

import java.util.ArrayList;
import java.util.Random;

import static com.danielweidensdoerfer.reaction.game.generator.Tag.*;

public class GridGenerator {

    public static final int CREATE_1_ITEM = 0;
    public static final int CREATE_ITEM_CAT = 1;
    public static final int CREATE_ALL_ITEMS = 2;

    public static final int COLORIZE_RANDOM = 0;
    public static final int COLORIZE_ITEM_BOUNDED = 1;
    public static final int COLORIZE_ITEM_CAT_BOUNDED = 2;

    private static int[] sColors;

    //social media
    private static Item sFacebook;
    private static Item sHandshake;
    private static Item sInformation;
    private static Item sInstagram;
    private static Item sLabel;
    private static Item sLike;
    private static Item sLinkedIn;
    private static Item sPinterest;
    private static Item sSnapchat;
    private static Item sTwitter;
    private static Item sWhatsapp;
    private static Item sYoutube;

    //weather
    private static Item sCloudRain;
    private static Item sCloudRainThunder;
    private static Item sCloudSnow;
    private static Item sCloudSun;
    private static Item sClouds;
    private static Item sMoon;
    private static Item sMoonCloud;
    private static Item sMoonCloudRain;
    private static Item sSun;
    private static Item sSunCloud;
    private static Item sSunCloudRain;

    //emojis
    private static Item sAdmired;
    private static Item sAngry;
    private static Item sFunny;
    private static Item sLove;
    private static Item sSad;

    //currency
    private static Item sDollar;
    private static Item sDoublePound;
    private static Item sEuro;
    private static Item sPeso;
    private static Item sPound;
    private static Item sRupiah;
    private static Item sWon;
    private static Item sYen;

    //numbers
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

    private static  Random random = new Random();

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
                //ContextCompat.getColor(context, R.color._13),
                ContextCompat.getColor(context, R.color._14),
                ContextCompat.getColor(context, R.color._15),
                ContextCompat.getColor(context, R.color._16),
//                ContextCompat.getColor(context, R.color._17),
//                ContextCompat.getColor(context, R.color._18),
//                ContextCompat.getColor(context, R.color._19),
//                ContextCompat.getColor(context, R.color._20)
        };

        long id = 0;

        //social media
        sFacebook = new Item(id++, context, R.drawable.img_scm_facebook, SOCIAL_MEDIA);
        sHandshake = new Item(id++, context, R.drawable.img_scm_handshake, SOCIAL_MEDIA);
        sInformation = new Item(id++, context, R.drawable.img_scm_information, SOCIAL_MEDIA);
        sInstagram = new Item(id++, context, R.drawable.img_scm_instagram, SOCIAL_MEDIA);
        sLabel = new Item(id++, context, R.drawable.img_scm_label, SOCIAL_MEDIA);
        sLike = new Item(id++, context, R.drawable.img_scm_like, SOCIAL_MEDIA);
        sLinkedIn = new Item(id++, context, R.drawable.img_scm_linkedin, SOCIAL_MEDIA);
        sPinterest = new Item(id++, context, R.drawable.img_scm_pinterest, SOCIAL_MEDIA);
        sSnapchat = new Item(id++, context, R.drawable.img_scm_snapchat, SOCIAL_MEDIA);
        sTwitter = new Item(id++, context, R.drawable.img_scm_twitter, SOCIAL_MEDIA);
        sWhatsapp = new Item(id++, context, R.drawable.img_scm_whatsapp, SOCIAL_MEDIA);
        sYoutube = new Item(id++, context, R.drawable.img_scm_youtube, SOCIAL_MEDIA);

        sItems.add(sFacebook);
        sItems.add(sHandshake);
        sItems.add(sInformation);
        sItems.add(sInstagram);
        sItems.add(sLabel);
        sItems.add(sLike);
        sItems.add(sLinkedIn);
        sItems.add(sPinterest);
        sItems.add(sSnapchat);
        sItems.add(sTwitter);
        sItems.add(sWhatsapp);
        sItems.add(sYoutube);

        //weather
        sCloudRain = new Item(id++, context, R.drawable.img_wth_cloud_rain, WEATHER);
        sCloudRainThunder = new Item(id++, context, R.drawable.img_wth_cloud_rain_thunder, WEATHER);
        sCloudSnow = new Item(id++, context, R.drawable.img_wth_cloud_snow, WEATHER);
        sCloudSun = new Item(id++, context, R.drawable.img_wth_cloud_sun, WEATHER);
        sClouds = new Item(id++, context, R.drawable.img_wth_clouds, WEATHER);
        sMoon = new Item(id++, context, R.drawable.img_wth_moon, WEATHER);
        sMoonCloud = new Item(id++, context, R.drawable.img_wth_moon_cloud, WEATHER);
        sMoonCloudRain = new Item(id++, context, R.drawable.img_wth_moon_cloud_rain, WEATHER);
        sSun = new Item(id++, context, R.drawable.img_wth_sun, WEATHER);
        sSunCloud = new Item(id++, context, R.drawable.img_wth_sun_cloud, WEATHER);
        sSunCloudRain = new Item(id++, context, R.drawable.img_wth_sun_cloud_rain, WEATHER);

        sItems.add(sCloudRain);
        sItems.add(sCloudRainThunder);
        sItems.add(sCloudSnow);
        sItems.add(sCloudSun);
        sItems.add(sClouds);
        sItems.add(sMoon);
        sItems.add(sMoonCloud);
        sItems.add(sMoonCloudRain);
        sItems.add(sSun);
        sItems.add(sSunCloud);
        sItems.add(sSunCloudRain);

        //emojis
        sAdmired = new Item(id++, context, R.drawable.img_emj_admired, EMOJI);
        sAngry = new Item(id++, context, R.drawable.img_emj_angry, EMOJI);
        sFunny = new Item(id++, context, R.drawable.img_emj_funny, EMOJI);
        sLove = new Item(id++, context, R.drawable.img_emj_love, EMOJI);
        sSad = new Item(id++, context, R.drawable.img_emj_sad, EMOJI);

        sItems.add(sAdmired);
        sItems.add(sAngry);
        sItems.add(sFunny);
        sItems.add(sLove);
        sItems.add(sSad);

        //currency
        sEuro = new Item(id++, context, R.drawable.img_cur_euro, CURRENCY);
        sDollar = new Item(id++, context, R.drawable.img_cur_dollar, CURRENCY);
        sDoublePound = new Item(id++, context, R.drawable.img_cur_double_pound, CURRENCY);
        sPeso = new Item(id++, context, R.drawable.img_cur_p, CURRENCY);
        sPound = new Item(id++, context, R.drawable.img_cur_pound, CURRENCY);
        sRupiah = new Item(id++, context, R.drawable.img_cur_rp, CURRENCY);
        sYen = new Item(id++, context, R.drawable.img_cur_yen, CURRENCY);
        sWon = new Item(id++, context, R.drawable.img_cur_won, CURRENCY);

        sItems.add(sEuro);
        sItems.add(sDollar);
        sItems.add(sDoublePound);
        sItems.add(sPeso);
        sItems.add(sPound);
        sItems.add(sRupiah);
        sItems.add(sWon);
        sItems.add(sYen);

        //numbers
        sOne = new Item(id++, context, R.drawable.img_num_1, NUMBER);
        sTwo = new Item(id++, context, R.drawable.img_num_2, NUMBER);
        sThree = new Item(id++, context, R.drawable.img_num_3, NUMBER);
        sFour = new Item(id++, context, R.drawable.img_num_4, NUMBER);
        sFive = new Item(id++, context, R.drawable.img_num_5, NUMBER);
        sSix = new Item(id++, context, R.drawable.img_num_6, NUMBER);
        sSeven = new Item(id++, context, R.drawable.img_num_7, NUMBER);
        sEight = new Item(id++, context, R.drawable.img_num_8, NUMBER);
        sNine = new Item(id++, context, R.drawable.img_num_9, NUMBER);
        sZero = new Item(id++, context, R.drawable.ic_zero, NUMBER);

        sItems.add(sOne);
        sItems.add(sTwo);
        sItems.add(sThree);
        sItems.add(sFour);
        sItems.add(sFive);
        sItems.add(sSix);
        sItems.add(sSeven);
        sItems.add(sEight);
        sItems.add(sNine);
        sItems.add(sZero);

        //fruits
        sBanana = new Item(id++, context, R.drawable.img_fr_banana, FRUIT);
        sApple = new Item(id++, context, R.drawable.img_fr_apple, FRUIT);
        sMelon = new Item(id++, context, R.drawable.img_fr_melon, FRUIT);
        sPear = new Item(id++, context, R.drawable.img_fr_pear, FRUIT);

        sItems.add(sBanana);
        sItems.add(sApple);
        sItems.add(sMelon);
        sItems.add(sPear);
    }

    public static Item[][] generate(int round) {
        //int cols = random.nextInt(7)+3;
        //int rows = random.nextInt(7)+3;
        int cols = 7;
        int rows = 11;
        int total = cols*rows;
        Item[][] result = new Item[cols][rows];

        create(result, CREATE_ITEM_CAT);

        colorize(result, COLORIZE_ITEM_BOUNDED);
        return result;
    }

    private static void create(Item[][] field, int mode) {
        switch(mode) {
            case CREATE_1_ITEM:
                ArrayList<Item> oneItem = new ArrayList<>();
                oneItem.add(sItems.get(random.nextInt(sItems.size())));
                create(field, oneItem, 1);
                break;

            case CREATE_ITEM_CAT:
                //find tag
                int tag = tags[random.nextInt(tags.length)];
                //find all items
                ArrayList<Item> tagItems = new ArrayList<>();
                for (Item item : sItems) {
                    if(item.tags.contains(tag)) tagItems.add(item);
                }
                //create for chosen items
                create(field, tagItems, tagItems.size());
                break;

            case CREATE_ALL_ITEMS:
                //create for all items
                create(field, sItems, 5);
                break;
        }
    }

    private static void create(final Item[][] field, final ArrayList<Item> itemCollection, final int itemCount) {
        int cols = field.length;
        int rows = field[0].length;
        int total = rows*cols;
        ArrayList<Item> pickedItems = new ArrayList<>();
        //find items
        for (int i = 0; i < itemCount; i++) {
            Item rItem;
            do {
                int rand = random.nextInt(itemCollection.size());
                rItem = itemCollection.get(rand);
            } while(pickedItems.contains(rItem));
            pickedItems.add(rItem);
        }
        //prepare itemRest for even item counts
        int[] itemRest = new int[itemCount];
        int index = 0;
        for(int i = 0; i < total; i++) {
            if(index == itemRest.length) index = 0;
            itemRest[index++]++;
        }
        //set items
        for(int i = 0; i < cols; i++) {
            for(int j = 0; j < rows; j++) {
                int itemIndex;
                do {
                    itemIndex = random.nextInt(pickedItems.size());
                }while(itemRest[itemIndex] <= 0);

                itemRest[itemIndex]--;

                field[i][j] = pickedItems.get(itemIndex).copy();
            }
        }
    }

    private static void colorize(Item[][] field, int mode) {
        int cols = field.length;
        int rows = field[0].length;
        int total = rows*cols;
        int colorCount = 0;
        ArrayList<Integer> colors = new ArrayList<>();
        switch(mode) {
            case COLORIZE_RANDOM:
                colorCount = 5;
                //find some colors
                for(int i = 0; i < colorCount; i++) {
                    int rColor;
                    do {
                        rColor = sColors[random.nextInt(sColors.length)];
                    } while(colors.contains(rColor));
                    colors.add(rColor);
                }
                //prepare colorRest for even color counts
                int[] colorRest = new int[colorCount];
                int index1 = 0;
                for(int i = 0; i < total; i++) {
                    if(index1 == colorRest.length) index1 = 0;
                    colorRest[index1++]++;
                }
                //set colors
                for(int i = 0; i < cols; i++) {
                    for(int j = 0; j < rows; j++) {
                        int colorIndex;
                        do {
                            colorIndex = random.nextInt(colors.size());
                        }while(colorRest[colorIndex] <= 0);

                        colorRest[colorIndex]--;

                        field[i][j].color = colors.get(colorIndex);
                    }
                }
                break;

            case COLORIZE_ITEM_BOUNDED:
                //find item type count
                ArrayList<Long> matchedIds = new ArrayList<>();
                for(int i = 0; i < cols; i++) {
                    for(int j = 0; j < rows; j++) {
                        long id = field[i][j].id;
                        if(matchedIds.contains(id)) continue;
                        matchedIds.add(id);
                    }
                }
                //find colors
                for(int i = 0; i < matchedIds.size(); i++) {
                    int rColor;
                    do {
                        rColor = sColors[random.nextInt(sColors.length)];
                    } while(colors.contains(rColor));
                    colors.add(rColor);
                }
                //set colors
                for(int i = 0; i < cols; i++) {
                    for(int j = 0; j < rows; j++) {
                        int index2 = matchedIds.indexOf(field[i][j].id);
                        field[i][j].color = colors.get(index2);
                    }
                }
                break;

            case COLORIZE_ITEM_CAT_BOUNDED:

                break;
        }
    }


}
