package com.danielweidensdoerfer.reaction.game;

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
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.danielweidensdoerfer.reaction.R;
import com.danielweidensdoerfer.reaction.ReactionActivity;

public class TimerView extends View {

    //constants
    private static final float STROKE_WIDTH = 4;

    //text
    private StaticLayout mTLayout;
    private TextPaint mTPaint;
    private int mTLWidth;
    private float mTScale = 1;
    private float mTX, mTY;

    private String mText = "";

    //circle
    private Paint mCPaint, mBPaint;
    private float mBScale = 0;

    //metrics
    private float mWidth, mHeight, mCenterX, mCenterY;

    private ReactionActivity mAct;

    private Handler mHandler;

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

        mTPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTPaint.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        mTPaint.setColor(ContextCompat.getColor(context, R.color.pink));
        mTPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.timerTextSize));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mCenterX = w/2;
        mCenterY = h/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mCenterX, mCenterY, mWidth/2, mCPaint);

        canvas.save();
        canvas.scale(mBScale, mBScale, mWidth/2, mHeight/2);
        canvas.drawCircle(mCenterX, mCenterY, mWidth/2-STROKE_WIDTH/2, mBPaint);
        canvas.restore();

        if(mTLayout != null) {
            canvas.save();
            canvas.scale(mTScale, mTScale, mWidth/2, mHeight/2);
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
        openAnim.setStartDelay(delay);
        openAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        openAnim.setDuration(200);
        openAnim.start();

        delay += 50;
        mHandler.postDelayed(() -> {
            mAct.gameView.show();
        }, delay);

    }

    public void start() {
        if(true) return;
        //countdown
        ObjectAnimator countdown = ObjectAnimator.ofInt(this, "time", Integer.parseInt(mText), 0);
        countdown.setInterpolator(new LinearInterpolator());
        countdown.setDuration(Integer.parseInt(mText)*1000);
        countdown.start();
    }

    private void createTextLayouts() {
        mTLWidth = (int)mWidth;
        mTLayout = new StaticLayout(mText, mTPaint, mTLWidth, Layout.Alignment.ALIGN_CENTER,
                1, 0, false);
        mTX = mCenterX - mTLWidth /2;
        mTY = mCenterY - mTLayout.getHeight()/2;
    }

    public void setBScale(float scale) {
        mBScale = scale;
        invalidate();
    }

    public void setTime(int time) {
        mText = String.valueOf(time);
        createTextLayouts();
        invalidate();
    }

}
