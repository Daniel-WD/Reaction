package com.danielweidensdoerfer.reaction.game;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.danielweidensdoerfer.reaction.R;
import com.danielweidensdoerfer.reaction.ReactionActivity;

public class CrossView extends View {

    //metrics
    private float mWidth, mHeight, mBlockSize;
    private float mBlockPadding;

    private float[][] mValues;
    private AnimatedVectorDrawable[] mCrosses;
    private int mIndex = 0;
    private int mCount = 3;

    //paint
    private Paint mStrokePaint;

    //activity
    private ReactionActivity mAct;

    //drawable
    private AnimatedVectorDrawable mCross;

    public CrossView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mAct = (ReactionActivity) context;
        mBlockPadding = mAct.getResources().getDimensionPixelSize(R.dimen.previewBlockPadding);

        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setColor(ContextCompat.getColor(context, R.color.pink));
        mStrokePaint.setStrokeWidth(3);
        mStrokePaint.setStyle(Paint.Style.STROKE);

        mCross = (AnimatedVectorDrawable) context.getDrawable(R.drawable.anim_draw_cross);
        setOnClickListener(v -> nextCross());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

        init();
    }

    private void init() {
        mBlockSize = Math.min(mWidth/ mCount, mHeight);

        float xOffset, yOffset;

        float fieldWidth = mCount*mBlockSize;
        float fieldHeight = mBlockSize;
        xOffset = (mWidth-fieldWidth)/2f;
        yOffset = (mHeight-fieldHeight)/2f;

        mBlockSize -= 2*mBlockPadding;

        mValues = new float[mCount][7];
        for(int i = 0; i < mCount; i++) {
            mValues[i][0] = xOffset + mBlockPadding + i*2*mBlockPadding + mBlockSize*i;//x
            mValues[i][1] = yOffset + mBlockPadding;//y
            mValues[i][2] = 1;//alpha
            mValues[i][3] = 1;//scale
            mValues[i][4] = 0;//rotate
            mValues[i][5] = 0;//tx
            mValues[i][6] = 0;//ty
        }
        mCross.setBounds(0, 0, (int)mBlockSize, (int)mBlockSize);

        mCrosses = new AnimatedVectorDrawable[mCount];
        for (int i = 0; i < mCrosses.length; i++) {
            mCrosses[i] = (AnimatedVectorDrawable) mCross.getConstantState().newDrawable().mutate();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i = 0; i < mCount; i++) {
//          canvas.drawRect(mValues[i][0], mValues[i][1],
//                  mValues[i][0] + mBlockSize, mValues[i][1] + mBlockSize,
//                  paint);

            canvas.save();
//            mTargets[i].item().drawable.setAlpha((int) (mValues[i][2] * 255));

            canvas.scale(mValues[i][3], mValues[i][3], mValues[i][0] + mBlockSize/2, mValues[i][1] + mBlockSize/2);
            canvas.translate(mValues[i][0] + mValues[i][5], mValues[i][1] + mValues[i][6]);
            canvas.rotate(mValues[i][4], mBlockSize/2, mBlockSize/2);

//            canvas.drawRect(0, 0, mBlockSize, mBlockSize, mStrokePaint);
//            mTargets[i].item().drawable.setColorFilter(mTargets[i].item().color, PorterDuff.Mode.SRC_ATOP);

            mCrosses[i].draw(canvas);
            canvas.restore();
        }
    }

    public void setCount(int count) {
        mCount = count;
        mIndex = 0;
        init();
        invalidate();
    }

    public void nextCross() {
        if(mIndex >= mCount) {
            mAct.gameManager.lose();
            return;
        }
        mCrosses[mIndex++].start();
        ValueAnimator updater = ValueAnimator.ofFloat(0, 1);
        updater.addUpdateListener(animation -> invalidate());
        updater.setDuration(mAct.getResources().getInteger(R.integer.cross_rot_dur));
        updater.start();
    }

    public void show() {
        setVisibility(VISIBLE);
        setTranslationY(mHeight);
        animate()
                .setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(200)
                .translationY(0)
                .start();
    }

}
