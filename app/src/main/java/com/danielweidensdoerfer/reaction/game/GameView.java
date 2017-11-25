package com.danielweidensdoerfer.reaction.game;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.renderscript.Sampler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import com.danielweidensdoerfer.reaction.R;
import com.danielweidensdoerfer.reaction.ReactionActivity;

public class GameView extends View {

    //drawing
    private Paint mStrokePaint;

    //metrics
    private float mWidth, mHeight;
    private float mBlockSize, mBlockPadding;

    private int mRows = 6, mCols = 4;

    private float mXOffset = 0, mYOffset = 0;

    private float[][][] mValues;/*cols|rows|x, y, alpha, scale, rotation*/

    private ReactionActivity mAct;

    private Handler mHandler;

    private Item[][] mItemField;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        GridGenerator.init(context);

        mAct = (ReactionActivity) context;

        mHandler = new Handler();

        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setColor(ContextCompat.getColor(context, R.color.pink));
        mStrokePaint.setStrokeWidth(1);
        mStrokePaint.setStyle(Paint.Style.STROKE);

        mBlockPadding = getResources().getDimensionPixelSize(R.dimen.blockPadding);

        mItemField = GridGenerator.generate(mRows, mCols);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        mBlockSize = Math.min(w/ mCols, h/ mRows);

        float fieldWidth = mCols*mBlockSize;
        float fieldHeight = mRows*mBlockSize;
        mXOffset = (mWidth-fieldWidth)/2f;
        mYOffset = (mHeight-fieldHeight)/2f;

        mBlockSize -= 2*mBlockPadding;

        mValues = new float[mCols][mRows][5];
        for(int i = 0; i < mCols; i++) {
            for(int j = 0; j < mRows; j++) {
                mValues[i][j][0] = mXOffset + mBlockPadding + i*2*mBlockPadding + mBlockSize*i;
                mValues[i][j][1] = mYOffset + mBlockPadding + j*2*mBlockPadding + mBlockSize*j;
                mValues[i][j][2] = 1f;
                mValues[i][j][3] = 1f;
                mValues[i][j][4] = 0;
            }
        }

        //mDrawable.setIntrinsicWidth((int)mBlockSize);
        //mDrawable.setIntrinsicHeight((int)mBlockSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

/*        for(int i = 0; i <= mRows; i++) {
            canvas.drawLine(0, mYOffset + mBlockSize*i,
                    mWidth, mYOffset + mBlockSize*i, mStrokePaint);
        }
        for(int i = 0; i <= mCols; i++) {
            canvas.drawLine(mXOffset + mBlockSize*i, 0, mXOffset + mBlockSize*i, mHeight, mStrokePaint);
        }*/

        for(int i = 0; i < mCols; i++) {
            for(int j = 0; j < mRows; j++) {
//                canvas.drawRect(mValues[i][j][0], mValues[i][j][1],
//                        mValues[i][j][0] + mBlockSize, mValues[i][j][1] + mBlockSize,
//                        mStrokePaint);

                canvas.save();
                mItemField[i][j].drawable.setAlpha((int)(mValues[i][j][2] * 255));
                mItemField[i][j].drawable.setBounds(0, 0, (int)mBlockSize, (int)mBlockSize);

                canvas.scale(mValues[i][j][3], mValues[i][j][3], mValues[i][j][0] + mBlockSize/2, mValues[i][j][1] + mBlockSize/2);
                canvas.translate(mValues[i][j][0], mValues[i][j][1]);
                canvas.rotate(mValues[i][j][4], mBlockSize/2, mBlockSize/2);

                mItemField[i][j].drawable.draw(canvas);
                canvas.restore();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int[] pos = findColRowByLocation(event.getX(), event.getY());
                if(pos == null) return false;
                ValueAnimator fadeOut = ValueAnimator.ofFloat(1, 0);
                fadeOut.setDuration(200);
                fadeOut.setInterpolator(new AccelerateInterpolator(0.5f));
                fadeOut.addUpdateListener(animation -> {
                    float v = (float) animation.getAnimatedValue();
                    mValues[pos[0]][pos[1]][2] = v;
                    mValues[pos[0]][pos[1]][3] = v;
                    mValues[pos[0]][pos[1]][4] = v * 360 * 2;
                    invalidate();
                });
                fadeOut.start();
                return false;
        }
        return super.onTouchEvent(event);
    }

    void show() {
        setVisibility(VISIBLE);

        long delay = 0;

        setAlpha(0);
        animate().setInterpolator(new AccelerateDecelerateInterpolator())
                .setStartDelay(delay)
                .alpha(1)
                .setDuration(100)
                .start();

        delay += 200;
        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(animation -> {
            float v = (float) animation.getAnimatedFraction();
            for(int i = 0; i < mCols; i++) {
                for(int j = 0; j < mRows; j++) {
                    mValues[i][j][2] = v;
                    mValues[i][j][3] = v;
//                    mValues[i][j][4] = 360 * v;
                }
            }
            invalidate();
        });
        anim.setStartDelay(delay);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(200);
//        anim.start();
    }

    private int[] findColRowByLocation(float sx, float sy){
        float px, py;
        for(int i = 0; i < mCols; i++) {
            for(int j = 0; j < mRows; j++) {
                px = mValues[i][j][0];
                py = mValues[i][j][1];
                if(px < sx && py < sy && px+mBlockSize > sx && py+mBlockSize > sy) {
                    return new int[] {i, j};
                }
            }
        }
        return null;
    }
}
