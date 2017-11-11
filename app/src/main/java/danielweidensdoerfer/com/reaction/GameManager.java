package danielweidensdoerfer.com.reaction;

import android.animation.Animator;
import android.graphics.Point;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;

public class GameManager {

    private ReactionActivity mAct;
    private int mActWidth, mActHeight;

    int numRound = 1;
    String task = "Remove all white circles";

    GameManager(ReactionActivity activity) {
        mAct = activity;

        Point size = new Point();
        mAct.getWindowManager().getDefaultDisplay().getSize(size);
        mActWidth = size.x;
        mActHeight = size.y;
    }

    void startGame() {
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
                                mAct.getResources().getDrawable(R.drawable.play_to_circle, mAct.getTheme());
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
            float cRadius = ((mAct.btnStart.getWidth())/2 -padding) *mAct.fStart.getScaleX();
            mAct.loadingView.setCRadius(cRadius);
            mAct.loadingView.blowUp();
            mAct.fStart.setVisibility(View.INVISIBLE);
        }, delay);
    }

}
