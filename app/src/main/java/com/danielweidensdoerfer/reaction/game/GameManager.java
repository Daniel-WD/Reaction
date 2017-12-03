package com.danielweidensdoerfer.reaction.game;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.danielweidensdoerfer.reaction.R;
import com.danielweidensdoerfer.reaction.ReactionActivity;
import com.danielweidensdoerfer.reaction.game.generator.GridGenerator;
import com.danielweidensdoerfer.reaction.utils.ViewAnimUtils;
import com.danielweidensdoerfer.reaction.utils.ViewUtils;

public class GameManager {

    private ReactionActivity mAct;
    private int mActWidth, mActHeight;

    public int time = 10; //seconds
    public int numRound = 1;
    public String task = "Remove all dollar and euro circles";

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
                    @Override public void onAnimationStart(Animator animation) {
                        //play to circle anim
                        AnimatedVectorDrawable drawable = (AnimatedVectorDrawable)
                                mAct.getResources().getDrawable(R.drawable.anim_play_to_circle, mAct.getTheme());
                        mAct.btnStart.setImageDrawable(drawable);
                        drawable.start();

                        //
                        mAct.handler.postDelayed(() -> {
                            mAct.btnStart.setImageDrawable(mAct.getDrawable(R.drawable.bg_start_overlay));
                        }, 2*mAct.getResources().getInteger(R.integer.pc_duration));
                    }
                    @Override public void onAnimationEnd(Animator animation) {
                        mAct.btnStart.animate().setListener(null);
                        mAct.fStart.setScaleX(mAct.btnStart.getScaleX());
                        mAct.fStart.setScaleY(mAct.btnStart.getScaleY());
                        mAct.btnStart.setScaleX(1);
                        mAct.btnStart.setScaleY(1);
                    }
                    @Override public void onAnimationCancel(Animator animation) {}
                    @Override public void onAnimationRepeat(Animator animation) {}
                })
                .setStartDelay(delay)
                .setDuration(300)
                .setInterpolator(new OvershootInterpolator(10))
                .scaleX(1.1f)
                .scaleY(1.1f)
                .start();
        delay += 100;

        //fade content out
        mAct.tvTitle.animate().setListener(new Animator.AnimatorListener() {
            @Override public void onAnimationEnd(Animator animation) {
                mAct.tvTitle.animate().setListener(null);
                ViewUtils.visibility(View.INVISIBLE, mAct.tvTitle, mAct.tvPoints, mAct.vDivider,
                        mAct.tvPlayedRounds, mAct.tvRoundRecord, mAct.tvRemovedObjects, mAct.vStartBg);
            }
            @Override public void onAnimationStart(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        float translateY = -mAct.tvPlayedRounds.getHeight()/4;
        ViewAnimUtils.translateYAlphaAnimOut(translateY, delay, 300, mAct.tvTitle,
                mAct.tvPoints, mAct.vDivider, mAct.tvPlayedRounds, mAct.tvRoundRecord,
                mAct.tvRemovedObjects);

        //move circle to center
        mAct.fStart.animate()
                .setDuration(200)
                .setStartDelay(delay)
                .setInterpolator(new OvershootInterpolator(1))
                .y(mActHeight/2- mAct.fStart.getHeight()/2)
                .start();
        delay += 200;

        //blow up
        mAct.handler.postDelayed(() -> {
            int padding = mAct.getResources().getDimensionPixelSize(R.dimen.btnStartPadding);
            float circleRadius = ((mAct.btnStart.getWidth())/2 -padding) *mAct.fStart.getScaleX();
            mAct.loadingView.setIRadius(circleRadius);
            nextRound();
            mAct.fStart.setVisibility(View.INVISIBLE);
        }, delay);
    }

    public void nextRound() {
        mAct.gameView.setItemField(GridGenerator.generate(1));
        mAct.loadingView.blowUp();
    }

    public void prepareTimer() {
        mAct.loadingView.setVisibility(View.INVISIBLE);
        mAct.timerView.setVisibility(View.VISIBLE);

        long delay = 0;

        //move timeView
        float startX = mActWidth/2-mAct.timerView.getWidth()/2;
        float startY = mActHeight/2-mAct.timerView.getHeight()/2;
        Path path = new Path();
        path.moveTo(startX, startY);
        path.cubicTo(startX+mActWidth/4, startY,
                mAct.timerView.getX(), startY-mActHeight/4,
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

}
