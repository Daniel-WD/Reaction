package com.danielweidensdoerfer.reaction.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.danielweidensdoerfer.reaction.R;
import com.danielweidensdoerfer.reaction.ReactionActivity;
import com.danielweidensdoerfer.reaction.game.generator.Target;

public class TargetLayout {

    private Paint paint;

    float mWidth, mHeight, mX, mY;
    private Target[] mTargets;
    private float[][] mValues;//x, y, alpha, scale, rotate, translationX, translationY
    private float mBlockSize, mBlockPadding;

    private int mCols;

    private ReactionActivity mAct;

    private int mElectricBlue;

    TargetLayout(Context context, float x, float y, float width, float height) {
        this.mWidth = width;
        this.mHeight = height;
        this.mX = x;
        this.mY = y;
        mAct = (ReactionActivity) context;

        mElectricBlue = ContextCompat.getColor(context, R.color.electric_blue);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.MAGENTA);
        paint.setStyle(Paint.Style.STROKE);

        mBlockPadding = mAct.getResources().getDimensionPixelSize(R.dimen.previewBlockPadding);
    }

    private void init() {
        mCols = mTargets.length;

        mBlockSize = Math.min(mWidth / mCols, mHeight);

        float xOffset, yOffset;

        float fieldWidth = mCols*mBlockSize;
        float fieldHeight = mBlockSize;
        xOffset = (mWidth -fieldWidth)/2f;
        yOffset = (mHeight -fieldHeight)/2f;

        mBlockSize -= 2*mBlockPadding;

        mValues = new float[mCols][7];
        for(int i = 0; i < mCols; i++) {
            mValues[i][0] = xOffset + mBlockPadding + i*2*mBlockPadding + mBlockSize*i;//mX
            mValues[i][1] = yOffset + mBlockPadding;//mY
            mValues[i][2] = 0;//alpha NOTE: start alpha is 0
            mValues[i][3] = 1;//scale
            mValues[i][4] = 0;//rotate
            mValues[i][5] = 0;//tx
            mValues[i][6] = 0;//ty
        }

        for(int i = 0; i < mCols; i++) {
            mTargets[i].item().drawable.setBounds(0, 0, (int)mBlockSize, (int)mBlockSize);
        }
    }

    void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(mX, mY);

        for(int i = 0; i < mCols; i++) {
//          canvas.drawRect(mValues[i][0], mValues[i][1],
//                  mValues[i][0] + mBlockSize, mValues[i][1] + mBlockSize,
//                  paint);

            canvas.save();
            mTargets[i].item().drawable.setAlpha((int) (mValues[i][2] * 255));

            canvas.scale(mValues[i][3], mValues[i][3], mValues[i][0] + mBlockSize/2, mValues[i][1] + mBlockSize/2);
            canvas.translate(mValues[i][0] + mValues[i][5], mValues[i][1] + mValues[i][6]);
            canvas.rotate(mValues[i][4], mBlockSize/2, mBlockSize/2);

            //canvas.drawRect(0, 0, mBlockSize, mBlockSize, mStrokePaint);
            mTargets[i].item().drawable.setColorFilter(mElectricBlue, PorterDuff.Mode.SRC_ATOP);
            mTargets[i].item().drawable.draw(canvas);
            canvas.restore();
        }

        canvas.restore();
    }

    void show() {
        long delay = 0;

        ValueAnimator alphaScaleAnim = ValueAnimator.ofFloat(0, 1);
        alphaScaleAnim.addUpdateListener(animation -> {
            for(int i = 0; i < mCols; i++) {
                float v = (float)animation.getAnimatedValue();
                mValues[i][2] = v;
                mValues[i][6] = (1-v)*mBlockSize/3;
                mAct.loadingView.invalidate();
            }
        });
        alphaScaleAnim.setStartDelay(delay);
        alphaScaleAnim.setInterpolator(new DecelerateInterpolator());
        alphaScaleAnim.setDuration(150);
        alphaScaleAnim.start();
    }

    void hide() {
        long delay = 0;

        ValueAnimator alphaScaleAnim = ValueAnimator.ofFloat(1, 0);
        alphaScaleAnim.addUpdateListener(animation -> {
            for(int i = 0; i < mCols; i++) {
                float v = (float)animation.getAnimatedValue();
                mValues[i][2] = v;
                mValues[i][6] = (1-v)*mBlockSize/3;
                mAct.loadingView.invalidate();
            }
        });
        alphaScaleAnim.setStartDelay(delay);
        alphaScaleAnim.setInterpolator(new AccelerateInterpolator());
        alphaScaleAnim.setDuration(150);
        alphaScaleAnim.start();
    }

    public void setTargets(Target[] targets) {
        this.mTargets = targets;
        init();
    }
}
