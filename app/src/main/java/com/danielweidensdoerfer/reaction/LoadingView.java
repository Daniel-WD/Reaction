package com.danielweidensdoerfer.reaction;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

public class LoadingView extends View {

    private StaticLayout mPTLayout, mSTLayout;
    private float mPTX, mPTY, mTranslationPTX = 0, mTranslationPTY = 0;
    private float mSTX, mSTY, mTranslationSTX = 0, mTranslationSTY = 0;
    private int mTLWidth;

    private Paint mCPaint, mBPaint, mIPaint, mOPaint, mODPaint;
    private TextPaint mPTPaint, mSTPaint;
    private float mCenterX, mCenterY, mWidth, mHeight;

    private float mCDegrees = 0;
    private float mIRadius = 0, mICenterRadius = 0, mIDegrees = 0;

    private float mOFill = 0;

    private RectF mMainRect, mBgRect;

    private String mPText = "", mSText = "";

    private float mDashesOffset = 0;

    private boolean mMirrorRO = false;

    private final float TEXT_REL_RADIUS_WIDTH = 1.8f; //relative
    private final float RADIUS_PADDING = 0.15f; //relative
    private final int CIRCLE_STROKE_WIDTH = 4;
    private final float OUTDOOR_OVERLAY_STRENGTH = 200;

    private Handler mHandler;

    private ReactionActivity mAct;

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mAct = (ReactionActivity) context;

        mHandler = new Handler();

        //colored circle
        mCPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCPaint.setColor(ContextCompat.getColor(getContext(), R.color.pink));
        mCPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCPaint.setStrokeWidth(CIRCLE_STROKE_WIDTH);

        //background circle
        mBPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBPaint.setColor(ContextCompat.getColor(getContext(), R.color.dark_dark_nero));
        mBPaint.setStyle(Paint.Style.FILL);

        //indicator
        mIPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIPaint.setColor(ContextCompat.getColor(getContext(), R.color.pink));
        mIPaint.setStyle(Paint.Style.FILL);

        //primary text
        mPTPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPTPaint.setColor(ContextCompat.getColor(getContext(), R.color.snow));
        mPTPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.currentRoundSize));
        mPTPaint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));

        //secondary text
        mSTPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mSTPaint.setColor(ContextCompat.getColor(getContext(), R.color.snow));
        mSTPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.currentTaskSize));
        mSTPaint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));

        //background overlay circle
        mOPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOPaint.setColor(ContextCompat.getColor(getContext(), R.color.nero));
        mOPaint.setStyle(Paint.Style.FILL);

        //background outdoor overlay circle
        mODPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mODPaint.setColor(ContextCompat.getColor(getContext(), R.color.nero));
        mODPaint.setStyle(Paint.Style.STROKE);
        mODPaint.setStrokeWidth(OUTDOOR_OVERLAY_STRENGTH);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w/2;
        mCenterY = h/2;
        mWidth = w;
        mHeight = h;

        float radius = calcMaxRadius();
        mMainRect = new RectF(mWidth/2-radius, mHeight/2-radius,
                mWidth - (mWidth/2-radius), mHeight/2+radius);
        radius -= CIRCLE_STROKE_WIDTH/2;
        mBgRect = new RectF(mWidth/2-radius, mHeight/2-radius,
                mWidth - (mWidth/2-radius), mHeight/2+radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //colored circle
        canvas.drawArc(mMainRect, -90, mCDegrees, mCDegrees > 0, mCPaint);

        //background
        canvas.drawCircle(mCenterX, mCenterY, calcMaxRadius()-CIRCLE_STROKE_WIDTH/2, mBPaint);

        //primary text layout
        if(mPTLayout != null) {
            canvas.save();
            updateTLPosition();
            canvas.translate(mPTX + mTranslationPTX, mPTY + mTranslationPTY);
            mPTLayout.draw(canvas);
            canvas.restore();
        }

        //secondary text layout
        if(mSTLayout != null) {
            canvas.save();
            updateTLPosition();
            canvas.translate(mSTX + mTranslationSTX, mSTY + mTranslationSTY);
            mSTLayout.draw(canvas);
            canvas.restore();
        }

        //Overlay
        canvas.drawArc(mBgRect, mOFill -90 - (mMirrorRO ? 180 : 0),
                360-2*(mOFill), false, mOPaint);

        //Outdoor
        //canvas.drawCircle(mCenterX, mCenterY, mMainRect.width()/2+OUTDOOR_OVERLAY_STRENGTH/2, mODPaint);

        //indicator circle
        float cx = mCenterX + (float)Math.sin(Math.toRadians(mIDegrees))*mICenterRadius;
        float cy = mCenterY - (float)Math.cos(Math.toRadians(mIDegrees))*mICenterRadius;
        canvas.drawCircle(cx, cy, mIRadius, mIPaint);
    }

    void blowUp() {
        setVisibility(VISIBLE);
        long delay = 0;

        /*mPText.replace(0, mPText.length(),
                getContext().getString(R.string.current_round_template, mAct.gameManager.numRound));
        createTextLayouts();*/

        //collapse indicator to 1/3
        ObjectAnimator collapseIOne = ObjectAnimator.ofFloat(this, "iRadius",
                mIRadius, mIRadius/3);
        collapseIOne.setDuration(220);
        collapseIOne.setInterpolator(new OvershootInterpolator());
        collapseIOne.start();

        //move indicator up
        ObjectAnimator moveUpI = ObjectAnimator.ofFloat(this, "iCenterRadius", mICenterRadius,
                calcMaxICenterRadius());
        moveUpI.setDuration(200);
        moveUpI.setInterpolator(new AccelerateDecelerateInterpolator());
        moveUpI.start();

        delay += 200;

        //create circle
        ObjectAnimator createCircle = ObjectAnimator.ofFloat(this, "iDegrees", 0, 360);
        createCircle.addUpdateListener(animation -> {
            mCDegrees = (float) animation.getAnimatedValue();
        });
        createCircle.setStartDelay(delay);
        createCircle.setInterpolator(new AccelerateDecelerateInterpolator());
        createCircle.setDuration(500);
        createCircle.start();

        delay += 500;

        //move indicator down
        ObjectAnimator moveIndicator = ObjectAnimator.ofFloat(this, "iCenterRadius",
                calcMaxICenterRadius(), -calcMaxICenterRadius());
        moveIndicator.addUpdateListener(animation -> {
            setOFill(animation.getAnimatedFraction()*180);
        });
        moveIndicator.setStartDelay(delay);
        moveIndicator.setInterpolator(new OvershootInterpolator(1));
        moveIndicator.setDuration(300);
        moveIndicator.start();

        delay += 300;

        //collapse indicator to 0
        ObjectAnimator collapseITwo = ObjectAnimator.ofFloat(this, "iRadius",
                mIRadius/3, 0);
        collapseITwo.setStartDelay(delay);
        collapseITwo.setInterpolator(new AnticipateInterpolator(2));
        collapseITwo.setDuration(200);
        collapseITwo.start();

        delay += 100;

        //enter round text - primary
        ObjectAnimator enterRoundText = ObjectAnimator.ofFloat(this, "translationPTY", -calcMaxRadius()/2, 0);
        enterRoundText.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mPTPaint.setAlpha((int)(255 * fraction));
        });
        enterRoundText.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
                mPText = mAct.getString(R.string.current_round_template, mAct.gameManager.numRound);
                createTextLayouts();
            }
            @Override public void onAnimationEnd(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        enterRoundText.setStartDelay(delay);
        enterRoundText.setDuration(200);
        enterRoundText.setInterpolator(new DecelerateInterpolator());
        enterRoundText.start();

        delay += 200 + getResources().getInteger(R.integer.showRoundDuration);

        //exit round text - primary
        ObjectAnimator exitRoundText = ObjectAnimator.ofFloat(this, "translationPTY", 0, calcMaxRadius()/2);
        exitRoundText.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mPTPaint.setAlpha((int)(255 * (1-fraction)));
        });
        exitRoundText.setStartDelay(delay);
        exitRoundText.setDuration(200);
        exitRoundText.setInterpolator(new AccelerateInterpolator());
        exitRoundText.start();

        delay += 100;

        //enter task text - secondary
        ObjectAnimator enterTaskText = ObjectAnimator.ofFloat(this, "translationSTY", -calcMaxRadius()/2, 0);
        enterTaskText.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mSTPaint.setAlpha((int)(255 * fraction));
        });
        enterTaskText.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
                mSText = mAct.gameManager.task;
                createTextLayouts();
            }
            @Override public void onAnimationEnd(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        enterTaskText.setStartDelay(delay);
        enterTaskText.setDuration(200);
        enterTaskText.setInterpolator(new DecelerateInterpolator());
        enterTaskText.start();

        delay += 200 + getResources().getInteger(R.integer.showTaskDuration);

        //exit round text - secondary
        ObjectAnimator exitTaskText = ObjectAnimator.ofFloat(this, "translationSTY", 0, calcMaxRadius()/2);
        exitTaskText.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mSTPaint.setAlpha((int)(255 * (1-fraction)));
        });
        exitTaskText.setStartDelay(delay);
        exitTaskText.setDuration(200);
        exitTaskText.setInterpolator(new AccelerateInterpolator());
        exitTaskText.start();

        delay += 200;

        //countdown
        //dash animation
        final float dashLength = (float)(Math.PI* mMainRect.width() /24f);
        ValueAnimator dashAnim = ValueAnimator.ofFloat(dashLength, 0);
        dashAnim.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            mCPaint.setPathEffect(new DashPathEffect(new float[]{value, dashLength-value-5/*prevent */},
                    (dashLength+3*value) - (mDashesOffset == dashLength || mDashesOffset == dashLength*3 ? 5*value : 0)));
            invalidate();
        });
        dashAnim.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {}
            @Override public void onAnimationEnd(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {
                mDashesOffset += dashLength;
            }
        });
        dashAnim.setRepeatMode(ValueAnimator.REVERSE);
        dashAnim.setRepeatCount(1);
        dashAnim.setStartDelay(delay);
        dashAnim.setDuration(1200);
        dashAnim.setInterpolator(new LinearInterpolator());
        dashAnim.start();

        long numberMoveDuration = 200;

        //enter three - primary
        ObjectAnimator enterThree = ObjectAnimator.ofFloat(this, "translationPTY", -calcMaxRadius()/3, 0);
        enterThree.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mPTPaint.setAlpha((int)(255 * fraction));
        });
        enterThree.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
                float countdownNumberSize =
                        getResources().getDimensionPixelSize(R.dimen.countdownNumberSize);
                mPTPaint.setTextSize(countdownNumberSize);
                mSTPaint.setTextSize(countdownNumberSize);
                mPTPaint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                mSTPaint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                mPText = mAct.getString(R.string.three);
                createTextLayouts();
            }
            @Override public void onAnimationEnd(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        enterThree.setStartDelay(delay);
        enterThree.setDuration(numberMoveDuration);
        enterThree.setInterpolator(new DecelerateInterpolator());
        enterThree.start();

        delay += numberMoveDuration + getResources().getInteger(R.integer.showCountdownNumberDuration);

        //exit three - primary
        ObjectAnimator exitThree = ObjectAnimator.ofFloat(this, "translationPTY", 0, calcMaxRadius()/3);
        exitThree.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mPTPaint.setAlpha((int)(255 * (1-fraction)));
        });
        exitThree.setStartDelay(delay);
        exitThree.setDuration(numberMoveDuration);
        exitThree.setInterpolator(new AccelerateInterpolator());
        exitThree.start();

        delay += numberMoveDuration;

        //enter two - secondary
        ObjectAnimator enterTwo = ObjectAnimator.ofFloat(this, "translationSTY", -calcMaxRadius()/3, 0);
        enterTwo.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mSTPaint.setAlpha((int)(255 * fraction));
        });
        enterTwo.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
                mSText = mAct.getString(R.string.two);
                createTextLayouts();
            }
            @Override public void onAnimationEnd(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        enterTwo.setStartDelay(delay);
        enterTwo.setDuration(numberMoveDuration);
        enterTwo.setInterpolator(new DecelerateInterpolator());
        enterTwo.start();

        delay += numberMoveDuration + getResources().getInteger(R.integer.showCountdownNumberDuration);

        //exit two - secondary
        ObjectAnimator exitTwo = ObjectAnimator.ofFloat(this, "translationSTY", 0, calcMaxRadius()/3);
        exitTwo.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mSTPaint.setAlpha((int)(255 * (1-fraction)));
        });
        exitTwo.setStartDelay(delay);
        exitTwo.setDuration(numberMoveDuration);
        exitTwo.setInterpolator(new AccelerateInterpolator());
        exitTwo.start();

        delay += numberMoveDuration;

        //enter one - primary
        ObjectAnimator enterOne = ObjectAnimator.ofFloat(this, "translationPTY", -calcMaxRadius()/3, 0);
        enterOne.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mPTPaint.setAlpha((int)(255 * fraction));
        });
        enterOne.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
                mPText = mAct.getString(R.string.one);
                createTextLayouts();
            }
            @Override public void onAnimationEnd(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        enterOne.setStartDelay(delay);
        enterOne.setDuration(numberMoveDuration);
        enterOne.setInterpolator(new DecelerateInterpolator());
        enterOne.start();

        delay += numberMoveDuration + getResources().getInteger(R.integer.showCountdownNumberDuration);

        //exit one - primary
        ObjectAnimator exitOne = ObjectAnimator.ofFloat(this, "translationPTY", 0, calcMaxRadius()/3);
        exitOne.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mPTPaint.setAlpha((int)(255 * (1-fraction)));
        });
        exitOne.setStartDelay(delay);
        exitOne.setDuration(numberMoveDuration);
        exitOne.setInterpolator(new AccelerateInterpolator());
        exitOne.start();

    }

    private void createTextLayouts() {
        mTLWidth = (int)(calcMaxRadius()* TEXT_REL_RADIUS_WIDTH);
        mPTLayout = new StaticLayout(mPText, mPTPaint, mTLWidth, Layout.Alignment.ALIGN_CENTER,
                1, 0, false);
        mSTLayout = new StaticLayout(mSText, mSTPaint, mTLWidth, Layout.Alignment.ALIGN_CENTER,
                1, 0, false);
    }

    private void updateTLPosition() {
        mPTX = mWidth/2 - mTLWidth /2;
        mPTY = mCenterY - mPTLayout.getHeight()/2;
        mSTX = mWidth/2 - mTLWidth /2;
        mSTY = mCenterY - mSTLayout.getHeight()/2;
    }

    private float calcMaxRadius() {
        return Math.min(mWidth/2 *(1-RADIUS_PADDING), mHeight/2 *(1-RADIUS_PADDING));
    }

    private float calcMaxICenterRadius() {
        return calcMaxRadius()-CIRCLE_STROKE_WIDTH/2;
    }

    public void setIRadius(float iRadius) {
        mIRadius = iRadius;
        invalidate();
    }

    public void setICenterRadius(float iCenterRadius) {
        mICenterRadius = iCenterRadius;
        invalidate();
    }

    public void setIDegrees(float iDegrees) {
        mIDegrees = iDegrees;
        invalidate();
    }

    public void setOFill(float oFill) {
        mOFill = oFill;
        invalidate();
    }

    public void setTranslationPTX(float tTX) {
        mTranslationPTX = tTX;
        invalidate();
    }

    public void setTranslationPTY(float tTY) {
        mTranslationPTY = tTY;
        invalidate();
    }

    public void setTranslationSTX(float tTX) {
        mTranslationSTX = tTX;
        invalidate();
    }

    public void setTranslationSTY(float tTY) {
        mTranslationSTY = tTY;
        invalidate();
    }
}
