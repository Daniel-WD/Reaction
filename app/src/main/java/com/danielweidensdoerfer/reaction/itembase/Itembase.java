package com.danielweidensdoerfer.reaction.itembase;

import android.content.Context;

import com.danielweidensdoerfer.reaction.R;

public class Itembase {

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

    public static Item[][] items;
    public static Item[] allItems;

    public static void init(Context context) {

        long id = 0;

        items = new Item[][] {
                {
                    //social media
                        new Item(id++, context, R.drawable.img_scm_facebook),
                        new Item(id++, context, R.drawable.img_scm_handshake),
                        new Item(id++, context, R.drawable.img_scm_information),
                        new Item(id++, context, R.drawable.img_scm_instagram),
                        new Item(id++, context, R.drawable.img_scm_label),
                        new Item(id++, context, R.drawable.img_scm_like),
                        new Item(id++, context, R.drawable.img_scm_linkedin),
                        new Item(id++, context, R.drawable.img_scm_pinterest),
                        new Item(id++, context, R.drawable.img_scm_snapchat),
                        new Item(id++, context, R.drawable.img_scm_twitter),
                        new Item(id++, context, R.drawable.img_scm_whatsapp),
                        new Item(id++, context, R.drawable.img_scm_youtube)
                },
                {
                        //weather
                        new Item(id++, context, R.drawable.img_wth_cloud_rain),
                        new Item(id++, context, R.drawable.img_wth_cloud_rain_thunder),
                        new Item(id++, context, R.drawable.img_wth_cloud_snow),
                        new Item(id++, context, R.drawable.img_wth_cloud_sun),
                        new Item(id++, context, R.drawable.img_wth_clouds),
                        new Item(id++, context, R.drawable.img_wth_moon),
                        new Item(id++, context, R.drawable.img_wth_moon_cloud),
                        new Item(id++, context, R.drawable.img_wth_moon_cloud_rain),
                        new Item(id++, context, R.drawable.img_wth_sun),
                        new Item(id++, context, R.drawable.img_wth_sun_cloud),
                        new Item(id++, context, R.drawable.img_wth_sun_cloud_rain)
                },
                {
                        //emojis
                        new Item(id++, context, R.drawable.img_emj_admired),
                        new Item(id++, context, R.drawable.img_emj_angry),
                        new Item(id++, context, R.drawable.img_emj_funny),
                        new Item(id++, context, R.drawable.img_emj_love),
                        new Item(id++, context, R.drawable.img_emj_sad)
                },
                {
                        //currency
                        new Item(id++, context, R.drawable.img_cur_euro),
                        new Item(id++, context, R.drawable.img_cur_dollar),
                        new Item(id++, context, R.drawable.img_cur_double_pound),
                        new Item(id++, context, R.drawable.img_cur_p),
                        new Item(id++, context, R.drawable.img_cur_pound),
                        new Item(id++, context, R.drawable.img_cur_rp),
                        new Item(id++, context, R.drawable.img_cur_yen),
                        new Item(id++, context, R.drawable.img_cur_won)
                },
                {
                        //numbers
                        new Item(id++, context, R.drawable.img_num_1),
                        new Item(id++, context, R.drawable.img_num_2),
                        new Item(id++, context, R.drawable.img_num_3),
                        new Item(id++, context, R.drawable.img_num_4),
                        new Item(id++, context, R.drawable.img_num_5),
                        new Item(id++, context, R.drawable.img_num_6),
                        new Item(id++, context, R.drawable.img_num_7),
                        new Item(id++, context, R.drawable.img_num_8),
                        new Item(id++, context, R.drawable.img_num_9),
                        new Item(id++, context, R.drawable.img_num_0)
                }
        };

        //all items into one dimen array
        int count = 0;
        for(int i = 0; i < items.length; i++) {
            for(int j = 0; j < items[i].length; j++) {
                count++;
            }
        }
        allItems = new Item[count];
        int i = 0;
        for(Item[] itemCat : items) {
            for(Item item : itemCat) {
                allItems[i++] = item;
            }
        }

    }


}
