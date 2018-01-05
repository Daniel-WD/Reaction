package com.danielweidensdoerfer.reaction.game;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.danielweidensdoerfer.reaction.R;

public class GameBackground extends View {

    private float mWidth, mHeight;
    private float mStrokeWidth;
    private float mLScale = 0;

    private Paint mStrokePaint;
    private int mRed, mGreen;

    public GameBackground(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mStrokeWidth = context.getResources().getDimensionPixelSize(R.dimen.strokeWidth);

        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setStrokeWidth(mStrokeWidth);
        mStrokePaint.setStyle(Paint.Style.STROKE);

        mRed = ContextCompat.getColor(context, R.color.red);
        mGreen = ContextCompat.getColor(context, R.color.green);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mStrokePaint.setStrokeWidth(mStrokeWidth/getScaleY());
        canvas.drawLine(mWidth/2 * (1-mLScale), mStrokeWidth/2, mWidth/2 + mWidth/2 * mLScale, mStrokeWidth/2, mStrokePaint);
        canvas.drawLine(mWidth/2 * (1-mLScale), mHeight-mStrokeWidth/2, mWidth/2 + mWidth/2 * mLScale, mHeight-mStrokeWidth/2, mStrokePaint);
    }

    public void showLines(boolean win) {
        updateColor(win);
        ObjectAnimator anim = ObjectAnimator.ofFloat(this, "lScale", 0, 1);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(300);
        anim.start();
    }

    public void hideLines(boolean win) {
        updateColor(win);
        ObjectAnimator anim = ObjectAnimator.ofFloat(this, "lScale", 1, 0);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(200);
        anim.start();
    }

    private void updateColor(boolean win) {
        mStrokePaint.setColor(win ? mGreen : mRed);
    }

    public void setLScale(float mLScale) {
        this.mLScale = mLScale;
        invalidate();
    }
}
