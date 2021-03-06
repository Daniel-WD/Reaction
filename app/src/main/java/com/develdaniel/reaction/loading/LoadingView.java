package com.develdaniel.reaction.loading;

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

import com.develdaniel.reaction.R;
import com.develdaniel.reaction.ReactionActivity;

public class LoadingView extends View {

    //constants
    private static final float TEXT_REL_RADIUS_WIDTH = 1.8f; //relative
    private static final float RADIUS_PADDING = 0.15f; //relative
    private static final int CIRCLE_STROKE_WIDTH = 4;
    private static final float OUTDOOR_OVERLAY_STRENGTH = 200;

    //metrics
    private float mCenterX, mCenterY, mWidth, mHeight;

    //text stuff
    private StaticLayout mPTLayout, mSTLayout;
    private TextPaint mPTPaint, mSTPaint;
    private float mPTX, mPTY, mTranslationPTX = 0, mTranslationPTY = 0;
    private float mSTX, mSTY, mTranslationSTX = 0, mTranslationSTY = 0;
    private int mTLWidth;
    private String mPText = "", mSText = "";
    private float mPTScale = 1f, mSTScale = 1f;

    //Targetlayout
    private TargetLayout mTargetLayout;

    //circle stuff
    private RectF mMainRect, mBgRect;

    private Paint mCPaint, mBPaint, mIPaint, mOPaint, mODPaint/*unused*/;
    private float mIRadius = 0, mICenterRadius = 0, mIDegrees = 0;
    private float mCDegrees = 0;
    private float mCScale = 1;
    private float mBScale = 1;
    private float mOFill = 0;

    private float mDashesOffset = 0;

    //handler
    private Handler mHandler;

    //activity
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

        //secondary text
        mSTPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mSTPaint.setColor(ContextCompat.getColor(getContext(), R.color.snow));

        //background overlay circle
        mOPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOPaint.setColor(ContextCompat.getColor(getContext(), R.color.nero));
        mOPaint.setStyle(Paint.Style.FILL);

        //background outdoor overlay circle
        mODPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mODPaint.setColor(ContextCompat.getColor(getContext(), R.color.nero));
        mODPaint.setStyle(Paint.Style.STROKE);
        mODPaint.setStrokeWidth(OUTDOOR_OVERLAY_STRENGTH);

        init();
    }

    public void init() {
        mPTPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.currentRoundSize));
        mPTPaint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));

        mSTPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.currentTaskSize));
        mSTPaint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));

        mTranslationPTX = 0;
        mTranslationPTY = 0;
        mTranslationSTX = 0;
        mTranslationSTY = 0;
        mPText = "";
        mSText = "";
        mPTScale = 1f;
        mSTScale = 1f;

        mICenterRadius = 0;
        mIDegrees = 0;
        mCDegrees = 0;
        mCScale = 1;
        mBScale = 1;
        mOFill = 0;
        mDashesOffset = 0;
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

        float targetsHeight = radius/2f;
        mTargetLayout = new TargetLayout(getContext(), mBgRect.left, mBgRect.centerY()-targetsHeight/3,
                mBgRect.width(), targetsHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //colored circle
        canvas.save();
        canvas.scale(mCScale, mCScale, mWidth/2, mHeight/2);
        canvas.drawArc(mMainRect, -90, mCDegrees, mCDegrees > 0, mCPaint);
        canvas.restore();

        //background
        canvas.save();
        canvas.scale(mBScale, mBScale, mWidth/2, mHeight/2);
        canvas.drawCircle(mCenterX, mCenterY, calcMaxRadius()-CIRCLE_STROKE_WIDTH/2, mBPaint);
        canvas.restore();

        //primary text layout
        if(mPTLayout != null) {
            canvas.save();
            canvas.scale(mPTScale, mPTScale, mWidth/2, mHeight/2);
            canvas.translate(mPTX + mTranslationPTX, mPTY + mTranslationPTY);
            mPTLayout.draw(canvas);
            canvas.restore();
        }

        //secondary text layout
        if(mSTLayout != null) {
            canvas.save();
            canvas.scale(mSTScale, mSTScale, mWidth/2, mHeight/2);
            canvas.translate(mSTX + mTranslationSTX, mSTY + mTranslationSTY);
            mSTLayout.draw(canvas);
            canvas.restore();
        }

        //Overlay
        canvas.drawArc(mBgRect, mOFill -90,
                360-2*(mOFill), false, mOPaint);

        //Outdoor
        //canvas.drawCircle(mCenterX, mCenterY, mMainRect.mWidth()/2+OUTDOOR_OVERLAY_STRENGTH/2, mODPaint);

        //indicator circle
        float cx = mCenterX + (float)Math.sin(Math.toRadians(mIDegrees))*mICenterRadius;
        float cy = mCenterY - (float)Math.cos(Math.toRadians(mIDegrees))*mICenterRadius;
        canvas.drawCircle(cx, cy, mIRadius, mIPaint);

        mTargetLayout.draw(canvas);
    }

    public void blowUp() {
        init();
        setVisibility(VISIBLE);
        long delay = 0;

        mTargetLayout.setTargets(mAct.gameManager.generatorResult.targets);

        //collapse indicator to 1/3
        mAct.ivCrossTick.setVisibility(View.INVISIBLE);

        ObjectAnimator collapseIOne = ObjectAnimator.ofFloat(this, "iRadius",
                mIRadius, mIRadius/3);
        collapseIOne.setDuration(220);
        collapseIOne.setInterpolator(new OvershootInterpolator());
        collapseIOne.start();

        //move indicator up
        ObjectAnimator moveUpI = ObjectAnimator.ofFloat(this, "iCenterRadius", mICenterRadius,
                calcMaxICenterRadius());
        moveUpI.setDuration(250);
        moveUpI.setInterpolator(new AccelerateDecelerateInterpolator());
        moveUpI.start();

        delay += 250;

        //create circle
        ObjectAnimator createCircle = ObjectAnimator.ofFloat(this, "iDegrees", 0, 360);
        createCircle.addUpdateListener(animation -> {
            mCDegrees = (float) animation.getAnimatedValue();
        });
        createCircle.setStartDelay(delay);
        createCircle.setInterpolator(new AccelerateDecelerateInterpolator());
        createCircle.setDuration(400);
        createCircle.start();

        delay += 400;

        //move indicator down and overlay fill
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
                mPText = mAct.getString(R.string.current_round_template, mAct.gameManager.currentRound);
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

        delay += 200;

        //enter remove text - secondary
        ObjectAnimator enterRemoveText = ObjectAnimator.ofFloat(this, "translationSTY", -calcMaxRadius()/2, 0);
        enterRemoveText.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mSTPaint.setAlpha((int)(255 * fraction));
        });
        enterRemoveText.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
                mSTPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.removeTextSize));
                mSText = mAct.getString(R.string.remove);
                createTextLayouts();
                mSTY = mTargetLayout.mY - mSTLayout.getHeight();
            }
            @Override public void onAnimationEnd(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        enterRemoveText.setStartDelay(delay);
        enterRemoveText.setDuration(200);
        enterRemoveText.setInterpolator(new DecelerateInterpolator());
        enterRemoveText.start();

        delay += 100;

        mHandler.postDelayed(() -> {
            mTargetLayout.show();
        }, delay);

        delay += mTargetLayout.mCols*getResources().getInteger(R.integer.showOneTargetDuration);

        mHandler.postDelayed(() -> {
            mTargetLayout.hide();
        }, delay);

        delay += 100;

        //exit remove text - secondary
        ObjectAnimator exitRemoveText = ObjectAnimator.ofFloat(this, "translationSTY", 0, calcMaxRadius()/2);
        exitRemoveText.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mSTPaint.setAlpha((int)(255 * (1-fraction)));
        });
        exitRemoveText.setStartDelay(delay);
        exitRemoveText.setDuration(200);
        exitRemoveText.setInterpolator(new AccelerateInterpolator());
        exitRemoveText.start();

        delay += 200;

        //enter time text - primary
        ObjectAnimator enterTimeText = ObjectAnimator.ofFloat(this, "translationPTY", -calcMaxRadius()/2, 0);
        enterTimeText.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mPTPaint.setAlpha((int)(255 * fraction));

            mPText = mAct.getString(R.string.time_template, (int)(mAct.gameManager.time * fraction));
            createTextLayouts();
        });
        enterTimeText.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
                mPTPaint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

                mPTPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.timeNumberSize));
            }
            @Override public void onAnimationEnd(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        enterTimeText.setStartDelay(delay);
        enterTimeText.setDuration(200);
        enterTimeText.setInterpolator(new DecelerateInterpolator());
        enterTimeText.start();

        delay += 200 + getResources().getInteger(R.integer.showTimeDuration);

        //exit time text - primary
        ObjectAnimator exitTimeText = ObjectAnimator.ofFloat(this, "translationPTY", 0, calcMaxRadius()/2);
        exitTimeText.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mPTPaint.setAlpha((int)(255 * (1-fraction)));
        });
        exitTimeText.setStartDelay(delay);
        exitTimeText.setDuration(200);
        exitTimeText.setInterpolator(new AccelerateInterpolator());
        exitTimeText.start();

        delay += 200;

        //--------------------------COUNTDOWN----------------------------
        //countdown

        long numberMoveDuration = 200;
        long numberShowDuration = getResources().getInteger(R.integer.showCountdownNumberDuration);

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
            @Override public void onAnimationEnd(Animator animation) {
                mCPaint.setPathEffect(null);
            }
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {
                mDashesOffset += dashLength;
            }
        });
        dashAnim.setRepeatMode(ValueAnimator.REVERSE);
        dashAnim.setRepeatCount(1);
        dashAnim.setStartDelay(delay);
        dashAnim.setDuration((3*numberShowDuration+5*numberMoveDuration)/2);
        dashAnim.setInterpolator(new LinearInterpolator());
        dashAnim.start();

        //enter three - secondary
        ObjectAnimator enterThree = ObjectAnimator.ofFloat(this, "translationSTY", -calcMaxRadius()/3, 0);
        enterThree.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mSTPaint.setAlpha((int)(255 * fraction));
        });
        enterThree.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
                float countdownNumberSize =
                        getResources().getDimensionPixelSize(R.dimen.countdownNumberSize);
                mPTPaint.setTextSize(countdownNumberSize);
                mSTPaint.setTextSize(countdownNumberSize);
                mPTPaint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                mSTPaint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                mSText = mAct.getString(R.string.num_three);
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

        delay += numberMoveDuration + numberShowDuration;

        //exit three - secondary
        ObjectAnimator exitThree = ObjectAnimator.ofFloat(this, "translationSTY", 0, calcMaxRadius()/3);
        exitThree.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mSTPaint.setAlpha((int)(255 * (1-fraction)));
        });
        exitThree.setStartDelay(delay);
        exitThree.setDuration(numberMoveDuration);
        exitThree.setInterpolator(new AccelerateInterpolator());
        exitThree.start();

        delay += numberMoveDuration;

        //enter two - primary
        ObjectAnimator enterTwo = ObjectAnimator.ofFloat(this, "translationPTY", -calcMaxRadius()/3, 0);
        enterTwo.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mPTPaint.setAlpha((int)(255 * fraction));
        });
        enterTwo.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
                mPText = mAct.getString(R.string.num_two);
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

        delay += numberMoveDuration + numberShowDuration;

        //exit two - primary
        ObjectAnimator exitTwo = ObjectAnimator.ofFloat(this, "translationPTY", 0, calcMaxRadius()/3);
        exitTwo.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mPTPaint.setAlpha((int)(255 * (1-fraction)));
        });
        exitTwo.setStartDelay(delay);
        exitTwo.setDuration(numberMoveDuration);
        exitTwo.setInterpolator(new AccelerateInterpolator());
        exitTwo.start();

        delay += numberMoveDuration;

        //enter one - secondary
        ObjectAnimator enterOne = ObjectAnimator.ofFloat(this, "translationSTY", -calcMaxRadius()/3, 0);
        enterOne.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mSTPaint.setAlpha((int)(255 * fraction));
        });
        enterOne.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
                mSText = mAct.getString(R.string.num_one);
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

        delay += numberMoveDuration + numberShowDuration;

        //exit one - secondary
        ObjectAnimator exitOne = ObjectAnimator.ofFloat(this, "translationSTY", 0, calcMaxRadius()/3);
        exitOne.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            if(fraction > 1) return;
            mSTPaint.setAlpha((int)(255 * (1-fraction)));
        });
        exitOne.setStartDelay(delay);
        exitOne.setDuration(numberMoveDuration);
        exitOne.setInterpolator(new AccelerateInterpolator());
        exitOne.start();

        delay += 100;

        //collapse background
        ObjectAnimator collapseBgAnim = ObjectAnimator.ofFloat(this, "bScale", 1, 0);
        collapseBgAnim.setStartDelay(delay);
        collapseBgAnim.setDuration(300);
        collapseBgAnim.setInterpolator(new AnticipateInterpolator(1.1f));
        collapseBgAnim.start();

        delay += 0;

        //collapse colored circle
        float scale = (float)getResources().getDimensionPixelSize(R.dimen.timeViewSize)/mMainRect.width();
        ObjectAnimator collapseCAnim = ObjectAnimator.ofFloat(this, "cScale", 1, scale);
        collapseCAnim.setStartDelay(delay);
        collapseCAnim.setDuration(300);
        collapseCAnim.setInterpolator(new AnticipateInterpolator(1.4f));
        collapseCAnim.start();

        delay += 300;

        mHandler.postDelayed(() -> {
            mAct.gameManager.prepareTimer();
        }, delay);
    }

    private void createTextLayouts() {
        //NOTE mSTX, mSTY, mPTX, mPTY will be set automatically
        mTLWidth = (int)(calcMaxRadius()* TEXT_REL_RADIUS_WIDTH);
        mPTLayout = new StaticLayout(mPText, mPTPaint, mTLWidth, Layout.Alignment.ALIGN_CENTER,
                1, 0, false);
        mSTLayout = new StaticLayout(mSText, mSTPaint, mTLWidth, Layout.Alignment.ALIGN_CENTER,
                1, 0, false);

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

    public void setBScale(float scale) {
        mBScale = scale;
        invalidate();
    }

    public void setCScale(float scale) {
        mCScale = scale;
    }
}
