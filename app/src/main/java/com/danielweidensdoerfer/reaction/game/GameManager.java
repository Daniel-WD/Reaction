package com.danielweidensdoerfer.reaction.game;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.danielweidensdoerfer.reaction.Database;
import com.danielweidensdoerfer.reaction.R;
import com.danielweidensdoerfer.reaction.ReactionActivity;
import com.danielweidensdoerfer.reaction.game.generator.GeneratorResult;
import com.danielweidensdoerfer.reaction.game.generator.GridGenerator;
import com.danielweidensdoerfer.reaction.utils.ViewAnimUtils;
import com.danielweidensdoerfer.reaction.utils.ViewUtils;

public class GameManager {

    private ReactionActivity mAct;
    private int mActWidth, mActHeight;

    public static final int BONUS_FOR_TIME = 20;

    public int bonusPointsFactor = 0;
    public int bonusPoints = BONUS_FOR_TIME;
    public int newPoints = 0;
    public int points = 0;
    public int time = 0; //seconds
    public int currentRound = 0;
    public GeneratorResult generatorResult = null;

    public boolean isInLooseScreen = false;

    public GameManager(ReactionActivity activity) {
        mAct = activity;

        Point size = new Point();
        mAct.getWindowManager().getDefaultDisplay().getSize(size);
        mActWidth = size.x;
        mActHeight = size.y;

        GridGenerator.init(activity);
    }

    public void startGame() {
        long delay = 0;
        //blop the button
        mAct.vStartBg.animate()
                .setDuration(200)
                .setInterpolator(new AnticipateInterpolator(4))
                .scaleX(0)
                .scaleY(0)
                .start();

        delay += 150;

        //blop the button
        mAct.btnStart.animate()
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        //play to circle anim
                        AnimatedVectorDrawable drawable = (AnimatedVectorDrawable)
                                mAct.getResources().getDrawable(R.drawable.avd_play_to_circle, mAct.getTheme());
                        mAct.btnStart.setImageDrawable(drawable);
                        drawable.start();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mAct.btnStart.animate().setListener(null);
                        mAct.fStart.setScaleX(mAct.btnStart.getScaleX());
                        mAct.fStart.setScaleY(mAct.btnStart.getScaleY());
                        mAct.btnStart.setScaleX(1);
                        mAct.btnStart.setScaleY(1);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .setStartDelay(delay)
                .setDuration(300)
                .setInterpolator(new OvershootInterpolator(10))
                .scaleX(1.1f)
                .scaleY(1.1f)
                .start();
        delay += 100;

        //fade content out
        mAct.tvTitle.animate().withEndAction(() -> {
            mAct.tvTitle.animate().setListener(null);
            ViewUtils.visibility(View.INVISIBLE, mAct.tvTitle, mAct.tvPoints, mAct.vDivider,
                    mAct.tvPlayedRounds, mAct.tvRoundRecord, mAct.tvRemovedObjects);
        });
        float translateY = -mAct.tvPlayedRounds.getHeight()/4;
        ViewAnimUtils.translateYAlphaAnimOut(translateY, delay, 200, mAct.tvTitle,
                mAct.tvPoints, mAct.vDivider, mAct.tvPlayedRounds, mAct.tvRoundRecord,
                mAct.tvRemovedObjects);

        //move circle to center
        mAct.fStart.animate()
                .setDuration(200)
                .setStartDelay(delay)
                .setInterpolator(new OvershootInterpolator(1))
                .y(mActHeight/2 - mAct.fStart.getHeight()/2)
                .start();
        delay += 200;

        //start game
        mAct.handler.postDelayed(() -> {
            nextRound();
            mAct.fStart.setVisibility(View.INVISIBLE);
        }, delay);
    }

    public void nextRound() {
        Database.playedRounds++;
        currentRound++;
        generatorResult = GridGenerator.generate(currentRound);
        time = (int) generatorResult.time/1000;
        mAct.gameView.setItemField(generatorResult.field, generatorResult.targets);
        if(mAct.ivCrossTick.getVisibility() == View.VISIBLE) {
            mAct.loadingView.setIRadius(mAct.ivCrossTick.getWidth()*mAct.ivCrossTick.getScaleX()/2);
        } else {
            int padding = mAct.getResources().getDimensionPixelSize(R.dimen.btnStartPadding);
            float circleRadius = ((mAct.btnStart.getWidth())/2 - padding)*mAct.fStart.getScaleX();
            mAct.loadingView.setIRadius(circleRadius);
        }

        mAct.loadingView.blowUp();
    }

    public void prepareTimer() {
        mAct.loadingView.setVisibility(View.INVISIBLE);
        mAct.timerView.setVisibility(View.VISIBLE);

        long delay = 0;

        mAct.timerView.setTranslationX(0);
        mAct.timerView.setTranslationY(0);

        //move timeView
        float startX = mActWidth/2 - mAct.timerView.getWidth()/2;
        float startY = mActHeight/2 - mAct.timerView.getHeight()/2;
        Path path = new Path();
        path.moveTo(startX, startY);
        path.cubicTo(startX + mActWidth/4, startY,
                mAct.timerView.getX(), startY - mActHeight/4,
                mAct.timerView.getX(), mAct.timerView.getY());

        ObjectAnimator moveTVAnim = ObjectAnimator.ofFloat(mAct.timerView,
                "x", "y", path);
        moveTVAnim.setStartDelay(delay);
        moveTVAnim.setDuration(300);
        moveTVAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        moveTVAnim.start();

        delay += 200;

        mAct.handler.postDelayed(() -> {
            mAct.timerView.show();
        }, delay);
    }

    private long showAfter(boolean win) {
        long delay = !win ? mAct.getResources().getInteger(R.integer.cross_rot_dur) : 0;

        mAct.timerView.stop();
        mAct.gameView.setEnabled(false);
        mAct.handler.postDelayed(() -> {
            mAct.crossView.hide();
        }, delay);

        delay += 200;

        mAct.handler.postDelayed(() -> {
            mAct.gameView.hide();
        }, delay);

        delay += 300;

        //hide timerview
        mAct.handler.postDelayed(() -> {
            mAct.timerView.hide();
        }, delay);

        delay += 100;

        //move timeView to center relative-->translate
        Path path = new Path();
        float endX = mActWidth/2 - mAct.timerView.getWidth()/2;
        float endY = mActHeight/2 - mAct.timerView.getHeight()/2;
        float xDist = mAct.timerView.getX() - endX;
        float yDist = endY - mAct.timerView.getY();
        path.moveTo(0, 0);
        path.cubicTo(-xDist/2, 0,
                -xDist, yDist/2,
                -xDist, yDist);

        ObjectAnimator moveTVAnim = ObjectAnimator.ofFloat(mAct.timerView,
                "translationX", "translationY", path);
        moveTVAnim.setStartDelay(delay);
        moveTVAnim.setDuration(200);
        moveTVAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        moveTVAnim.start();

        //scale game bg
        mAct.gameBackground.setVisibility(View.VISIBLE);
        mAct.gameBackground.setPivotY(mAct.gameBackground.getHeight()/2);
        scaleGameBg(delay, 300, mAct.ivCrossTick);

        delay += 200;

        mAct.handler.postDelayed(() -> {
            //switch and show tick/cross
            mAct.timerView.setVisibility(View.INVISIBLE);
            mAct.ivCrossTick.setVisibility(View.VISIBLE);

            float s = (float) mAct.timerView.getWidth()/(float) mAct.ivCrossTick.getWidth();
            mAct.ivCrossTick.setScaleX(s);
            mAct.ivCrossTick.setScaleY(s);
            AnimatedVectorDrawable avdTickCross;
            if(win) {
                avdTickCross = (AnimatedVectorDrawable)
                        mAct.getResources().getDrawable(R.drawable.avd_circle_to_tick, mAct.getTheme());
            } else {
                avdTickCross = (AnimatedVectorDrawable)
                        mAct.getResources().getDrawable(R.drawable.avd_circle_to_cross, mAct.getTheme());
            }
            mAct.ivCrossTick.setImageDrawable(avdTickCross);
            avdTickCross.start();

            //scale tick
            mAct.ivCrossTick.animate()
                    .setStartDelay(0)
                    .setDuration(200)
                    .setInterpolator(new OvershootInterpolator(2))
                    .scaleX(1)
                    .scaleY(1)
                    .start();
        }, delay);

        delay += 100;

        //show lines
        mAct.handler.postDelayed(() -> {
            mAct.gameBackground.showLines(win);
        }, delay);

        //translate y of tick/cross
        mAct.handler.postDelayed(() -> {
            float tY = -mAct.tvGamePoints.getHeight();
            mAct.ivCrossTick.setTranslationY(tY);
            scaleGameBg(0, 200, mAct.ivCrossTick, mAct.tvGamePoints, mAct.tvNewGamePoints);
            mAct.ivCrossTick.setTranslationY(0);
            mAct.ivCrossTick.animate()
                    .setStartDelay(0)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(300)
                    .translationY(tY)
                    .start();
        }, delay);

        //color pink to green
        int pink = ContextCompat.getColor(mAct, R.color.pink);
        int greenRed;
        if(win) {
            greenRed = ContextCompat.getColor(mAct, R.color.green);
        } else {
            greenRed = ContextCompat.getColor(mAct, R.color.red);
        }
        ValueAnimator colorAnim = ValueAnimator.ofArgb(pink, greenRed);
        colorAnim.addUpdateListener(animation -> {
            int color = (int) animation.getAnimatedValue();
            mAct.ivCrossTick.setColorFilter(color);
        });
        colorAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        colorAnim.setStartDelay(delay);
        colorAnim.setDuration(300);
        colorAnim.start();

        delay += 300;

        newPoints = generatorResult.points;
        bonusPointsFactor = Integer.parseInt(mAct.timerView.getText());
        fillPoints();

        //show current points
        mAct.tvGamePoints.setY(mActHeight/2 + mAct.ivCrossTick.getHeight()/2 - mAct.tvGamePoints.getHeight());
        mAct.handler.postDelayed(() -> {
            mAct.tvGamePoints.setVisibility(View.VISIBLE);
            float oldTY = mAct.tvGamePoints.getTranslationY();
            mAct.tvGamePoints.setTranslationY(mAct.tvGamePoints.getHeight() + oldTY);
            mAct.tvGamePoints.setAlpha(0);
            mAct.tvGamePoints.animate()
                    .setStartDelay(0)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(200)
                    .translationY(oldTY)
                    .alpha(1)
                    .start();
        }, delay);

        delay += 300;

        return delay;
    }

    private void hideAfter(long delay, boolean win, boolean goHome) {

        isInLooseScreen = false;

        //hide current points
        mAct.handler.postDelayed(() -> {
            float oldTY = mAct.tvGamePoints.getTranslationY();
            mAct.tvGamePoints.animate()
                    .setStartDelay(0)
                    .setInterpolator(new AccelerateInterpolator())
                    .setDuration(200)
                    .translationY(mAct.tvGamePoints.getHeight() + oldTY)
                    .withEndAction(() -> mAct.tvGamePoints.setVisibility(View.INVISIBLE))
                    .alpha(0)
                    .start();
        }, delay);

        delay += 200;

        mAct.handler.postDelayed(() -> {
            //switch and show circle

            AnimatedVectorDrawable avdCircle;
            if(win) {
                avdCircle = (AnimatedVectorDrawable)
                        mAct.getResources().getDrawable(R.drawable.avd_tick_to_circle, mAct.getTheme());
            } else {
                avdCircle = (AnimatedVectorDrawable)
                        mAct.getResources().getDrawable(R.drawable.avd_cross_to_circle, mAct.getTheme());
            }
            mAct.ivCrossTick.setImageDrawable(avdCircle);
            avdCircle.start();

            //scale circle
            float s = (float) mAct.timerView.getWidth()/(float) mAct.ivCrossTick.getWidth();
            mAct.ivCrossTick.animate()
                    .setStartDelay(0)
                    .setDuration(200)
                    .setInterpolator(new AnticipateInterpolator(2))
                    .scaleX(s)
                    .scaleY(s)
                    .start();
        }, delay);

        delay += 0;

        //hide lines
        mAct.handler.postDelayed(() -> {
            mAct.gameBackground.hideLines(win);
        }, delay);

        delay += 0;

        //hide gamebackground
        mAct.handler.postDelayed(() -> {
            mAct.gameBackground.animate()
                    .setStartDelay(0)
                    .scaleY(1)
                    .alpha(0)
                    .setInterpolator(new AccelerateInterpolator())
                    .setDuration(300)
                    .start();
            ValueAnimator updater = ValueAnimator.ofFloat(1, 0);
            updater.addUpdateListener(animation -> mAct.gameBackground.invalidate());
            updater.setDuration(300);
            updater.setStartDelay(0);
            updater.start();
        }, delay);

        delay += 0;
        //translate y of circle
        mAct.handler.postDelayed(() -> {
//            float tY = -mAct.tvGamePoints.getHeight();
//            mAct.ivCrossTick.setTranslationY(tY);
//            scaleGameBg(0, 200, mAct.ivCrossTick, mAct.tvGamePoints, mAct.tvNewGamePoints);
//            mAct.ivCrossTick.setTranslationY(0);
            mAct.ivCrossTick.animate()
                    .setStartDelay(0)
                    .setInterpolator(new OvershootInterpolator(5))
                    .setDuration(400)
                    .translationY(0)
                    .start();
        }, delay);

        //color tick/cross to pink
        int pink = ContextCompat.getColor(mAct, R.color.pink);
        int greenRed;
        if(win) {
            greenRed = ContextCompat.getColor(mAct, R.color.green);
        } else {
            greenRed = ContextCompat.getColor(mAct, R.color.pink);
        }
        ValueAnimator colorAnimTwo = ValueAnimator.ofArgb(greenRed, pink);
        colorAnimTwo.addUpdateListener(animation -> {
            int color = (int) animation.getAnimatedValue();
            mAct.ivCrossTick.setColorFilter(color);
        });
        colorAnimTwo.setInterpolator(new AccelerateDecelerateInterpolator());
        colorAnimTwo.setStartDelay(delay);
        colorAnimTwo.setDuration(200);
        colorAnimTwo.start();

        delay += 400;

        if(!goHome) {
            mAct.handler.postDelayed(this::nextRound, delay);
        } else {
            //GO TO START SCREEN
            //move circle to center

            mAct.handler.postDelayed(() -> {
                long d = 0;

                int padding = mAct.getResources().getDimensionPixelSize(R.dimen.btnStartPadding);
                float circleRadius = ((mAct.btnStart.getWidth())/2 - padding);

                float w = mAct.ivCrossTick.getWidth()*mAct.ivCrossTick.getScaleX();
                float s = w/(2*circleRadius);
                mAct.fStart.setScaleX(s);
                mAct.fStart.setScaleY(s);

                mAct.ivCrossTick.setVisibility(View.INVISIBLE);
                mAct.fStart.setVisibility(View.VISIBLE);
                mAct.fStart.animate()
                        .setStartDelay(d)
                        .setDuration(300)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .translationY(0)
                        .scaleX(1)
                        .scaleY(1)
                        .start();

                mAct.vStartBg.animate()
                        .setStartDelay(d)
                        .setDuration(300)
                        .setInterpolator(new OvershootInterpolator(4))
                        .scaleX(1)
                        .scaleY(1)
                        .start();

                //play to circle anim
                AnimatedVectorDrawable drawable = (AnimatedVectorDrawable)
                        mAct.getResources().getDrawable(R.drawable.avd_circle_to_play, mAct.getTheme());
                mAct.btnStart.setImageDrawable(drawable);
                drawable.start();

                d += 300;

                ViewUtils.visibility(View.VISIBLE, mAct.tvTitle, mAct.tvPoints, mAct.vDivider,
                        mAct.tvPlayedRounds, mAct.tvRoundRecord, mAct.tvRemovedObjects);
                float translateY = -mAct.tvPlayedRounds.getHeight()/4;
                ViewAnimUtils.translateYAlphaAnimIn(translateY, d, 300, mAct.tvTitle,
                        mAct.tvPoints, mAct.vDivider, mAct.tvPlayedRounds, mAct.tvRoundRecord,
                        mAct.tvRemovedObjects);
            }, delay);
        }
    }

    public void closeGameField(boolean win) {

        long delay = showAfter(win);

        if(win) {
            delay = win(delay);
        } else {
            delay = loose(delay);
            return;
        }

        hideAfter(delay, win, false);

    }

    private long loose(long delay) {

        if(points > Database.recordPoints) {
            Database.recordPoints = points;
        }
        if(currentRound-1 > Database.roundRecord) {
            Database.roundRecord = currentRound-1;
        }

        currentRound = 0;
        points = 0;

        //show loose text
        mAct.tvLoose.setY(mAct.tvGamePoints.getY() + mAct.tvGamePoints.getHeight());
        mAct.handler.postDelayed(() -> {
            mAct.tvLoose.setVisibility(View.VISIBLE);
            mAct.tvLoose.setAlpha(0);
            mAct.tvLoose.setTranslationX(mActWidth/2);
            mAct.tvLoose.animate()
                    .setStartDelay(0)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(200)
                    .translationX(0)
                    .alpha(1)
                    .start();
        }, delay);

        delay += 200;

        //show repeat button
        //translate
        mAct.btnRepeat.setVisibility(View.VISIBLE);
        mAct.btnRepeat.setTranslationY(mActHeight - mAct.btnRepeat.getX());
        mAct.btnRepeat.animate()
                .setInterpolator(new OvershootInterpolator(1.2f))
                .translationY(0)
                .setDuration(400)
                .setStartDelay(delay)
                .start();
        //rotate
        mAct.btnRepeat.animate()
                .setInterpolator(new DecelerateInterpolator())
                .rotation(360*3)
                .setDuration(450)
                .setStartDelay(delay)
                .start();

        delay += 100;

        //show home button
        mAct.btnHome.setVisibility(View.VISIBLE);
        mAct.btnHome.setAlpha(0f);
        mAct.btnHome.animate()
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .alpha(1f)
                .setStartDelay(delay)
                .setDuration(100)
                .start();

        mAct.handler.postDelayed(() -> isInLooseScreen = true, delay);

        return delay;
    }

    public void closeLooseScreen(boolean repeat) {
        long delay = 0;

        //hide loose text
        mAct.handler.postDelayed(() -> {
            mAct.tvLoose.animate()
                    .setStartDelay(0)
                    .setInterpolator(new AccelerateInterpolator())
                    .setDuration(100)
                    .translationX(-mActWidth/2)
                    .alpha(0)
                    .withEndAction(() -> mAct.tvLoose.setVisibility(View.INVISIBLE))
                    .start();
        }, delay);

        delay += 50;
        //hide repeat button
        mAct.btnRepeat.animate()
                .setInterpolator(new AnticipateInterpolator(1f))
                .translationY(mActHeight - mAct.btnRepeat.getX())
                .setDuration(250)
                .setStartDelay(delay)
                .start();
        mAct.btnRepeat.animate()
                .setInterpolator(new AccelerateInterpolator())
                .rotation(360*10)
                .setDuration(250)
                .setStartDelay(delay)
                .withEndAction(() -> mAct.btnRepeat.setVisibility(View.VISIBLE))
                .start();

        //hide home button
        mAct.btnHome.animate()
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .alpha(0f)
                .setStartDelay(delay)
                .setDuration(100)
                .withEndAction(() -> mAct.btnHome.setVisibility(View.VISIBLE))
                .start();

        delay += 100;

        hideAfter(delay, false, !repeat);
    }

    private long win(long delay) {

        //POINT DISPOSER

        //show new points
        mAct.tvNewGamePoints.setY(mAct.tvGamePoints.getY() + mAct.tvGamePoints.getHeight());
        mAct.handler.postDelayed(() -> {
            mAct.tvNewGamePoints.setVisibility(View.VISIBLE);
            mAct.tvNewGamePoints.setAlpha(0);
            mAct.tvNewGamePoints.setTranslationX(mActWidth/2);
            mAct.tvNewGamePoints.animate()
                    .setStartDelay(0)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(200)
                    .translationX(0)
                    .alpha(1)
                    .start();
        }, delay);

        delay += 200;

        //Add new points to points
        final int startPoints = points;
        final int startNewPoints = newPoints;
        ValueAnimator counter = ValueAnimator.ofInt(newPoints, 0);
        counter.addUpdateListener(animation -> {
            int v = (int) animation.getAnimatedValue();
            float f = animation.getAnimatedFraction();
            newPoints = v;
            points = startPoints + (int) (startNewPoints*f);
            fillPoints();
        });
        counter.setDuration(300);
        counter.setStartDelay(delay);
        counter.setInterpolator(new AccelerateDecelerateInterpolator());
        counter.start();

        delay += 500;

        //hide new points
        mAct.handler.postDelayed(() -> {
            mAct.tvNewGamePoints.animate()
                    .setStartDelay(0)
                    .setInterpolator(new AccelerateInterpolator())
                    .setDuration(200)
                    .translationX(-mActWidth/2)
                    .alpha(0)
                    .withEndAction(() -> mAct.tvNewGamePoints.setVisibility(View.INVISIBLE))
                    .start();
        }, delay);

        //time bonus
        if(bonusPointsFactor > 0) {

            delay += 200;

            //show bonus text
            mAct.tvBonus.setY(mAct.tvGamePoints.getY() + mAct.tvGamePoints.getHeight());
            mAct.handler.postDelayed(() -> {
                mAct.tvBonus.setVisibility(View.VISIBLE);
                mAct.tvBonus.setAlpha(0);
                mAct.tvBonus.setTranslationX(mActWidth/2);
                mAct.tvBonus.animate()
                        .setStartDelay(0)
                        .setInterpolator(new DecelerateInterpolator())
                        .setDuration(200)
                        .translationX(0)
                        .alpha(1)
                        .start();
            }, delay);

            delay += 500;

            //hide bonus text
            mAct.handler.postDelayed(() -> {
                mAct.tvBonus.animate()
                        .setStartDelay(0)
                        .setInterpolator(new AccelerateInterpolator())
                        .setDuration(200)
                        .translationX(-mActWidth/2)
                        .alpha(0)
                        .withEndAction(() -> mAct.tvBonus.setVisibility(View.INVISIBLE))
                        .start();
            }, delay);

            delay += 200;

            //show time bonus points
            mAct.tvTimeGamePoints.setY(mAct.tvGamePoints.getY() + mAct.tvGamePoints.getHeight());
            mAct.handler.postDelayed(() -> {
                mAct.tvTimeGamePoints.setVisibility(View.VISIBLE);
                mAct.tvTimeGamePoints.setAlpha(0);
                mAct.tvTimeGamePoints.setTranslationX(mActWidth/2);
                mAct.tvTimeGamePoints.animate()
                        .setStartDelay(0)
                        .setInterpolator(new DecelerateInterpolator())
                        .setDuration(200)
                        .translationX(0)
                        .alpha(1)
                        .start();
            }, delay);

            delay += 200;

            //multiply bonus points
            final int startFactor = bonusPointsFactor;
            ValueAnimator multiplier = ValueAnimator.ofInt(bonusPointsFactor, 0);
            multiplier.addUpdateListener(animation -> {
                int v = (int) animation.getAnimatedValue();
                float f = animation.getAnimatedFraction();
                bonusPointsFactor = v;
                bonusPoints = (int) (BONUS_FOR_TIME + f*(float) (startFactor - 1)*(float) BONUS_FOR_TIME);
                fillPoints();
            });
            multiplier.setDuration(300);
            multiplier.setStartDelay(delay);
            multiplier.setInterpolator(new AccelerateDecelerateInterpolator());
            multiplier.start();

            delay += 500;

            //add bonus points
            mAct.handler.postDelayed(() -> {
                final int starterPoints = points;
                final int starterBonusPoints = bonusPoints;
                ValueAnimator increaser = ValueAnimator.ofInt(bonusPoints, 0);
                increaser.addUpdateListener(animation -> {
                    int v = (int) animation.getAnimatedValue();
                    float f = animation.getAnimatedFraction();
                    bonusPoints = v;
                    points = starterPoints + (int) (starterBonusPoints*f);
                    fillPoints();
                });
                increaser.setDuration(300);
                increaser.setInterpolator(new AccelerateDecelerateInterpolator());
                increaser.start();
            }, delay);

            delay += 500;

            //show time bonus points
            mAct.handler.postDelayed(() -> {
                mAct.tvTimeGamePoints.animate()
                        .setStartDelay(0)
                        .setInterpolator(new AccelerateInterpolator())
                        .setDuration(200)
                        .translationX(-mActWidth/2)
                        .withEndAction(() -> mAct.tvTimeGamePoints.setVisibility(View.INVISIBLE))
                        .alpha(0)
                        .start();
            }, delay);
        }

        delay += 200;

        return delay;
    }

    private void scaleGameBg(long delay, long duration, View... view) {
        float centerY = mActHeight/2;
        float maxY = centerY;
        float minY = centerY;
        for(View v : view) {
            maxY = Math.max(+v.getY() + v.getHeight(), maxY);
            minY = Math.min(+v.getY(), minY);
        }
        float distCenter = Math.max(maxY - centerY, centerY - minY);
        float scaleY = distCenter*2/(float) mAct.gameBackground.getHeight() + 0.02f/*padding :)*/;
        mAct.gameBackground.animate()
                .setStartDelay(delay)
                .scaleY(scaleY)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(duration)
                .start();
        ValueAnimator updater = ValueAnimator.ofFloat(1, 0);
        updater.addUpdateListener(animation -> mAct.gameBackground.invalidate());
        updater.setDuration(duration);
        updater.setStartDelay(delay);
        updater.start();
    }

    private void fillPoints() {
        mAct.tvNewGamePoints.setText(mAct.getString(R.string.add_points, newPoints));
        mAct.tvGamePoints.setText(mAct.getString(R.string.game_points_template, points));
        mAct.tvTimeGamePoints.setText(mAct.getString(R.string.time_bonus_points,
                bonusPoints, bonusPointsFactor));
    }

}
