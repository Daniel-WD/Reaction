package com.danielweidensdoerfer.reaction.game.generator;

import android.content.Context;
import android.util.Log;

import com.danielweidensdoerfer.reaction.ReactionActivity;
import com.danielweidensdoerfer.reaction.bases.Colorbase;
import com.danielweidensdoerfer.reaction.bases.itembase.Item;
import com.danielweidensdoerfer.reaction.bases.itembase.Itembase;

import java.util.ArrayList;
import java.util.Random;

public class GridGenerator {

    private static final String TAG = GridGenerator.class.getSimpleName();

    private static final int ADDITIONAL_POINTS = 100;
    private static final long ADDITIONAL_TIME = 1000;
    private static final int STD_ROUNDS = 3;
    private static final long START_TIME = 6000;
    private static final int STD_TARGETS = 1;

    private static final int CREATE_1_ITEM = 0;
    private static final int CREATE_ITEM_CAT = 1;
    private static final int CREATE_ALL_ITEMS = 2;

    private static final int COLORIZE_RANDOM = 0;
    private static final int COLORIZE_ITEM_BOUNDED = 1;
    private static final int COLORIZE_ITEM_CAT_BOUNDED = 2;


    private static int sConfigCount = 100;
    private static LevelConfig[] configs = new LevelConfig[] {
            new LevelConfig(100, 4, 4, 4, 5000, 4000, 1),
            new LevelConfig(200, 5, 5, 4, 6000, 4000, 1),
            new LevelConfig(300, 6, 5, 5, 9000, 7000, 1),
            new LevelConfig(400, 7, 6, 5, 12000, 10000, 1)
    };

    private static  Random random = new Random();

    public static void init(ReactionActivity act) {

        float gridRatio = (float)act.gameView.getWidth()/(float)act.gameView.getHeight();

        configs = new LevelConfig[sConfigCount];
        for(int i = 0; i < configs.length; i++) {
            configs[i] = createConfig(gridRatio, i);
        }

        for(LevelConfig config : configs) {
            Log.d("TAG", "Points:" + config.winPoints + " Rounds:" + config.numRounds + " Rows:" + config.rows
                    + " Cols:" + config.cols + " startTime:" + config.startTime + " endTime:" + config.endTime);
        }
    }

    private static LevelConfig createConfig(float ratio, int n) {
        int rows = 4;
        int cols = 4;
        for(int i = 0; i < n; i++) {
            float colDiff = Math.abs(((float)cols+1f)/(float)rows -ratio);
            float rowDiff = Math.abs((float)cols/((float)rows+1f) -ratio);
            if(colDiff < rowDiff) cols++; else rows++;
        }

        long startTime = START_TIME+n*ADDITIONAL_TIME;
        return new LevelConfig((n+1)*ADDITIONAL_POINTS, STD_ROUNDS, rows, cols,
                startTime, startTime-(STD_ROUNDS-1)*1000, STD_TARGETS);
    }

    private static LevelConfig findConfig(int round) {
        int r = 0;
        for(int i = 0; i < configs.length; i++) {
            if(configs[i].numRounds + r >= round) {
                configs[i].setCurrentRound(round-r);
                return configs[i];
            }
            r += configs[i].numRounds;
        }
        return null;
    }

    public static GeneratorResult generate(int round) {
        LevelConfig config = findConfig(round);

        Item[][] field = new Item[config.cols][config.rows];

        create(field, CREATE_ITEM_CAT);

        colorize(field, COLORIZE_RANDOM);

        Target[] targets = new Target[config.targets];

        fillTargets(targets, field);

        return new GeneratorResult(field, targets, config.time(), config.winPoints);
    }

    private static void fillTargets(Target[] targets, Item[][] field) {
        ArrayList<Item> items = new ArrayList<>();//all items in field
        ArrayList<Integer> colors = new ArrayList<>();//all colors in field
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if(!items.contains(field[i][j])) items.add(field[i][j]);
                if(!colors.contains(field[i][j].color)) colors.add(field[i][j].color);
            }
        }
        //choose target items and colors
        ArrayList<Item> chosenItems = new ArrayList<>();
        ArrayList<Integer> chosenColors = new ArrayList<>();
        for (int i = 0; i < targets.length; i++) {
            int r = random.nextInt(1);  //only item targets
            if(r == 0) {
                Item item;
                do {
                    item = items.get(random.nextInt(items.size()));
                } while(chosenItems.contains(item));
                chosenItems.add(item);
                targets[i] = new Target.ItemTarget(item.copy());
                Log.d(TAG, "new item");
            } else {
                int color;
                do {
                    color = colors.get(random.nextInt(colors.size()));
                } while(chosenColors.contains(color));
                chosenColors.add(color);
                targets[i] = new Target.ColorTarget(color);
                Log.d(TAG, "new color");
            }
        }
    }

    private static void create(Item[][] field, int mode) {
        Item[] itemSet = randItemSet();
        switch(mode) {
            case CREATE_1_ITEM:
                create(field, 1, itemSet[random.nextInt(itemSet.length)]);
                break;

            case CREATE_ITEM_CAT:
                //create for chosen items
                create(field, Math.min(5, itemSet.length), itemSet);// TODO: 05.01.2018 HERE IS THE ITEM COUNT
                break;

            case CREATE_ALL_ITEMS:
                //create for all items
                create(field, 5, Itembase.allItems);// TODO: 05.01.2018 HERE IS THE ITEM COUNT
                break;
        }
    }

    private static void create(final Item[][] field, final int itemCount, final Item... itemCollection) {
        int cols = field.length;
        int rows = field[0].length;
        int total = rows*cols;
        ArrayList<Item> pickedItems = new ArrayList<>();
        //find items
        for (int i = 0; i < itemCount; i++) {
            Item rItem;
            do {
                int rand = random.nextInt(itemCollection.length);
                rItem = itemCollection[rand];
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
        int colorCount;
        ArrayList<Integer> colors = new ArrayList<>();
        int[] colorSet = randColorSet();
        switch(mode) {
            case COLORIZE_RANDOM:
                colorCount = 5;// TODO: 05.01.2018 HERE IS THE COLOR COUNT
                //find some colors
                for(int i = 0; i < colorCount; i++) {
                    int rColor;
                    do {
                        rColor = colorSet[random.nextInt(colorSet.length)];
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
                        rColor = colorSet[random.nextInt(colorSet.length)];
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

    private static int[] randColorSet() {
        return Colorbase.colors[random.nextInt(Colorbase.colors.length)];
    }

    private static Item[] randItemSet() {
        return Itembase.items[random.nextInt(Itembase.items.length)];
    }
}
