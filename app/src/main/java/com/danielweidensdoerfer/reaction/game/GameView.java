package com.danielweidensdoerfer.reaction.game;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.danielweidensdoerfer.reaction.R;
import com.danielweidensdoerfer.reaction.ReactionActivity;
import com.danielweidensdoerfer.reaction.game.generator.GridGenerator;
import com.danielweidensdoerfer.reaction.game.generator.Item;

public class GameView extends View {

    //drawing
    private Paint mStrokePaint, mBorderPaint, mBackgroundPaint;

    //metrics
    private float mWidth, mHeight;
    private float mBlockSize, mBlockPadding;

    private float mDLScale = 0;

    private int mRows = 1, mCols = 1;

    private float mXOffset = 0, mYOffset = 0;

    private float[][][] mValues;/*cols|rows|x, y, alpha, scale, rotation, translationX, translationY*/
    private boolean[][] mClicked;

    private ReactionActivity mAct;

    private Handler mHandler;

    private Item[][] mItemField;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mAct = (ReactionActivity) context;

        mHandler = new Handler();

        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setColor(ContextCompat.getColor(context, R.color.snow));
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(0);

        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setColor(ContextCompat.getColor(context, R.color.snow));
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(1);

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(ContextCompat.getColor(context, R.color.dark_dark_nero));
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mBlockPadding = getResources().getDimensionPixelSize(R.dimen.blockPadding);
    }

    public void init() {
        mClicked = new boolean[mCols][mRows];
        for(int i = 0; i < mCols; i++) {
            for(int j = 0; j < mRows; j++){
                mClicked[i][j] = false;
            }
        }
        onSizeChanged(getWidth(), getHeight(), getWidth(), getHeight());
        for(int i = 0; i < mCols; i++) {
            for(int j = 0; j < mRows; j++){
                mItemField[i][j].drawable.setBounds(0, 0, (int)mBlockSize, (int)mBlockSize);
            }
        }
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

        mValues = new float[mCols][mRows][7];
        for(int i = 0; i < mCols; i++) {
            for(int j = 0; j < mRows; j++) {
                mValues[i][j][0] = mXOffset + mBlockPadding + i*2*mBlockPadding + mBlockSize*i;
                mValues[i][j][1] = mYOffset + mBlockPadding + j*2*mBlockPadding + mBlockSize*j;
                mValues[i][j][2] = 0f;
                mValues[i][j][3] = 1f;
                mValues[i][j][4] = 0;
                mValues[i][j][5] = 0;
                mValues[i][j][6] = 0;
            }
        }

        //mDrawable.setIntrinsicWidth((int)mBlockSize);
        //mDrawable.setIntrinsicHeight((int)mBlockSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //divider lines
        //horizontal
        for(int i = 1; i < mRows; i++) {
            for(int j = 0; j < mCols; j++) {
                canvas.drawLine(mXOffset + mBlockPadding + j*(2*mBlockPadding+mBlockSize) + (1-mDLScale)*mBlockSize/2,
                        mYOffset + (mBlockSize+2*mBlockPadding)*i,
                        mXOffset + mBlockPadding + j*(2*mBlockPadding+mBlockSize) + mBlockSize - (1-mDLScale)*mBlockSize/2,
                        mYOffset + (mBlockSize+2*mBlockPadding)*i,
                        mStrokePaint);
            }
        }
        //vertical
        for(int i = 1; i < mCols; i++) {
            for(int j = 0; j < mRows; j++) {
                canvas.drawLine(mXOffset + (mBlockSize+2*mBlockPadding)*i,
                        mYOffset + mBlockPadding + j*(2*mBlockPadding+mBlockSize) + (1-mDLScale)*mBlockSize/2,
                        mXOffset + (mBlockSize+2*mBlockPadding)*i,
                        mYOffset + mBlockPadding + j*(2*mBlockPadding+mBlockSize) + mBlockSize - (1-mDLScale)*mBlockSize/2,
                        mStrokePaint);
            }
        }

        /*
        for(int i = 0; i <= mCols; i++) {
            canvas.drawLine(mXOffset + mBlockSize*i, 0, mXOffset + mBlockSize*i, mHeight, mStrokePaint);
        }*/

        //items
        for(int i = 0; i < mCols; i++) {
            for(int j = 0; j < mRows; j++) {
//                canvas.drawRect(mValues[i][j][0], mValues[i][j][1],
//                        mValues[i][j][0] + mBlockSize, mValues[i][j][1] + mBlockSize,
//                        mStrokePaint);

                canvas.save();
                mItemField[i][j].drawable.setAlpha((int)(mValues[i][j][2] * 255));

                canvas.scale(mValues[i][j][3], mValues[i][j][3], mValues[i][j][0] + mBlockSize/2, mValues[i][j][1] + mBlockSize/2);
                canvas.translate(mValues[i][j][0] + mValues[i][j][5], mValues[i][j][1] + mValues[i][j][6]);
                canvas.rotate(mValues[i][j][4], mBlockSize/2, mBlockSize/2);

                //canvas.drawRect(0, 0, mBlockSize, mBlockSize, mStrokePaint);
                mItemField[i][j].drawable.setColorFilter(mItemField[i][j].color, PorterDuff.Mode.SRC_ATOP);
                mItemField[i][j].drawable.draw(canvas);
                canvas.restore();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                int[] pos = findColRowByLocation(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()));
                if(pos == null || mClicked[pos[0]][pos[1]]) return true;
                mClicked[pos[0]][pos[1]] = true;

                final float f = (event.getX(event.getActionIndex()) - (mValues[pos[0]][pos[1]][0] + mBlockSize/2)) / (mBlockSize/2);

                Path p = new Path();
                p.moveTo(0, 0);
                p.cubicTo(0.7f, 0, 1, 0.3f, 1, 1);
                PathMeasure pm = new PathMeasure(p, false);
                float[] pathPos = new float[2];

                ValueAnimator fadeOut = ValueAnimator.ofFloat(0, 1);
                fadeOut.setDuration(400);
                fadeOut.setInterpolator(new AccelerateInterpolator(0.5f));
                fadeOut.addUpdateListener(animation -> {
                    float v = (float) animation.getAnimatedValue();
                    //mValues[pos[0]][pos[1]][2] = 1-v;
                    //mValues[pos[0]][pos[1]][3] = 1+v;
                    mValues[pos[0]][pos[1]][4] = (f > 0 ? -1 : 1) * v * 360 * 1/2;

                    pm.getPosTan(pm.getLength()*v, pathPos, null);
                    mValues[pos[0]][pos[1]][5] = -f * mWidth/3 * pathPos[0];
                    mValues[pos[0]][pos[1]][6] = mHeight*1.1f * pathPos[1];

                    invalidate();
                });
                fadeOut.start();
                return true;
        }
        return super.onTouchEvent(event);
    }

    void show() {
        setVisibility(VISIBLE);

        long delay = 0;

        mAct.gameBackground.setVisibility(VISIBLE);
        mAct.gameBackground.setPivotY(mAct.gameBackground.getHeight()*2/3);
        mAct.gameBackground.setScaleY(0);
        mAct.gameBackground.animate()
                .setStartDelay(delay)
                .scaleY(1)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(250)
                .start();

        delay += 200;

        mHandler.postDelayed(() -> {
            mAct.timerView.start();
        }, delay);

        //show items
        for (int i = mRows-1; i >= 0; i--) {
            final int row = i;
            ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
            anim.addUpdateListener(animation -> {
                float v = (float) animation.getAnimatedValue();
                for(int j = 0; j < mCols; j++) {
                    mValues[j][row][2] = v;
                    mValues[j][row][6] = -(1-v)*mBlockSize/5;
                }
                invalidate();
            });
            anim.setStartDelay(delay);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(100);
            anim.start();
            delay += 30;
        }

        delay += 150;

        //show divider lines
        ObjectAnimator scaleDLs = ObjectAnimator.ofFloat(this, "dLScale", 0, 1);
        scaleDLs.setStartDelay(delay);
        scaleDLs.setDuration(200);
        scaleDLs.setInterpolator(new DecelerateInterpolator());
        scaleDLs.start();

    }

    private int[] findColRowByLocation(float sx, float sy){
        float px, py;
        for(int i = 0; i < mCols; i++) {
            for(int j = 0; j < mRows; j++) {
                px = mValues[i][j][0];
                py = mValues[i][j][1];
                if(px-mBlockPadding < sx && py-mBlockPadding < sy && px+mBlockSize+mBlockPadding > sx && py+mBlockSize+mBlockPadding > sy) {
                    return new int[] {i, j};
                }
            }
        }
        return null;
    }

    public void setItemField(Item[][] field) {
        if(field == null) return;
        mItemField = field;
        mCols = mItemField.length;
        mRows = mItemField[0].length;
        init();
        invalidate();
    }

    public void setDLScale(float scale) {
        mDLScale = scale;
        invalidate();
    }
}
