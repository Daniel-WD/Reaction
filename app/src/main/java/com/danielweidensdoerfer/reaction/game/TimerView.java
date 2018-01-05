package com.danielweidensdoerfer.reaction.game;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.danielweidensdoerfer.reaction.R;
import com.danielweidensdoerfer.reaction.ReactionActivity;

public class TimerView extends View {

    //constants
    private static final float C_STROKE_WIDTH = 10;
    private static final float W_STROKE_WIDTH = 4;

    //text
    private StaticLayout mTLayout;
    private TextPaint mTPaint;
    private int mTLWidth;
    private float mTScale = 1;
    private float mTX, mTY;

    private String mText = "";

    private boolean mStopped = false;

    //circle
    private Paint mCPaint, mBPaint, mWPaint;
    private float mBScale = 0;
    private float mWScale = 1;
    private float mWDegrees = 0;

    //metrics
    private float mWidth, mHeight, mCenterX, mCenterY;

    private ReactionActivity mAct;

    private Handler mHandler;

    //timer
    private ObjectAnimator mCountdown;

    public TimerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mAct = (ReactionActivity) context;

        mHandler = new Handler();

        mCPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCPaint.setStyle(Paint.Style.FILL);
        mCPaint.setColor(ContextCompat.getColor(context, R.color.pink));

        mBPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBPaint.setStyle(Paint.Style.FILL);
        mBPaint.setColor(ContextCompat.getColor(context, R.color.dark_dark_nero));

        mWPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWPaint.setStyle(Paint.Style.FILL);
        mWPaint.setColor(ContextCompat.getColor(context, R.color.snow));

        mTPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTPaint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        mTPaint.setColor(ContextCompat.getColor(context, R.color.pink));
        mTPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.timerTextSize));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mCenterX = mWidth/2;
        mCenterY = mHeight/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //white
        canvas.save();
        canvas.scale(mWScale * 1/getScaleX(), mWScale * 1/getScaleY(), mWidth/2, mHeight/2);
        canvas.drawArc(0, 0, mWidth, mHeight, -90, mWDegrees, true, mWPaint);
        canvas.restore();

        //color
        canvas.save();
        canvas.scale(1, 1, mWidth/2, mHeight/2);
        canvas.drawCircle(mCenterX, mCenterY, mWidth/2- (W_STROKE_WIDTH/getScaleX()) /2, mCPaint);
        canvas.restore();

        //background
        canvas.save();
        canvas.scale(mBScale, mBScale, mWidth/2, mHeight/2);
        canvas.drawCircle(mCenterX, mCenterY, mWidth/2- (C_STROKE_WIDTH + W_STROKE_WIDTH)/getScaleX() /2, mBPaint);
        canvas.restore();

        //text
        if(mTLayout != null) {
            canvas.save();
            canvas.scale(mTScale /* * 1/(1+(getScaleX()-1)*0.5f)*/, mTScale/* * 1/(1+(getScaleY()-1)*0.5f)*/, mWidth/2, mHeight/2);
            canvas.translate(mTX, mTY);
            mTLayout.draw(canvas);
            canvas.restore();
        }
    }

    public void show() {
        setTime(mAct.gameManager.time);

        long delay = 0;

        //open anim
        ObjectAnimator openAnim = ObjectAnimator.ofFloat(this, "bScale", 0, 1);
        openAnim.addUpdateListener(animation -> {
            float f = animation.getAnimatedFraction();
            mWDegrees = 360 * (f);
        });
        openAnim.setStartDelay(delay);
        openAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        openAnim.setDuration(200);
        openAnim.start();

        delay += 50;
        mHandler.postDelayed(() -> {
            mAct.gameView.show();
            mAct.crossView.show();
        }, delay);
    }

    public void hide() {
        long delay = 0;

        mWDegrees = 0;
        //close anim
        ObjectAnimator closeAnim = ObjectAnimator.ofFloat(this, "bScale", 1, 0);
        closeAnim.setStartDelay(delay);
        closeAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        closeAnim.setDuration(200);
        closeAnim.start();

    }

    public void start() {
        //mCountdown
        mStopped = false;
        mCountdown = ObjectAnimator.ofInt(this, "time", Integer.parseInt(mText), 0);
        mCountdown.addUpdateListener(animation -> {
            float f = animation.getAnimatedFraction();
            mWDegrees = 360 * (1-f);
        });
        mCountdown.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationEnd(Animator animation) {
                if(mStopped) {
                    mStopped = false;
                    return;
                }
                mAct.gameManager.closeGameField(false);
            }
            @Override public void onAnimationStart(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        mCountdown.setInterpolator(new LinearInterpolator());
        mCountdown.setDuration(Integer.parseInt(mText)*1000);
        mCountdown.start();
    }

    public void stop() {
        mStopped = true;
        mCountdown.cancel();
    }

    private void createTextLayouts() {
        mTLWidth = (int)mWidth;
        mTLayout = new StaticLayout(mText, mTPaint, mTLWidth, Layout.Alignment.ALIGN_CENTER,
                1, 0, false);
        mTX = mCenterX - mTLWidth /2;
        mTY = mCenterY - mTLayout.getHeight()/2;
    }

    public void setTime(int time) {
        mText = String.valueOf(time);
        createTextLayouts();
        invalidate();
    }

    public void setBScale(float scale) {
        mBScale = scale;
        invalidate();
    }

    public void setWScale(float scale) {
        mWScale = scale;
        invalidate();
    }

    public String getText() {
        return mText;
    }
}
