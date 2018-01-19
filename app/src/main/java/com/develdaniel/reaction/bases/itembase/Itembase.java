package com.develdaniel.reaction.bases.itembase;

import android.content.Context;

import com.develdaniel.reaction.R;

public class Itembase {

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
                },
                {
                        //numbers
                        new Item(id++, context, R.drawable.img_dif_bit),
                        new Item(id++, context, R.drawable.img_dif_boomerang),
                        new Item(id++, context, R.drawable.img_dif_c_effect),
                        new Item(id++, context, R.drawable.img_dif_d),
                        new Item(id++, context, R.drawable.img_dif_d_coin),
                        new Item(id++, context, R.drawable.img_dif_fane),
                        new Item(id++, context, R.drawable.img_dif_leave),
                        new Item(id++, context, R.drawable.img_dif_lite),
                        new Item(id++, context, R.drawable.img_dif_m),
                        new Item(id++, context, R.drawable.img_dif_n),
                        new Item(id++, context, R.drawable.img_dif_neptun),
                        new Item(id++, context, R.drawable.img_dif_quill),
                        new Item(id++, context, R.drawable.img_dif_rope),
                        new Item(id++, context, R.drawable.img_dif_saphir),
                        //new Item(id++, context, R.drawable.img_dif_t_circles), //similar to boomerang
                        new Item(id++, context, R.drawable.img_dif_three),
                        new Item(id++, context, R.drawable.img_dif_z)
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
