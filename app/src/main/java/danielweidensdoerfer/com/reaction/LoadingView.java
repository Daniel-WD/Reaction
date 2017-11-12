package danielweidensdoerfer.com.reaction;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;

public class LoadingView extends View {

    private DynamicLayout mTLayout;
    private float mTX, mTY;
    private int mTLWidth;

    private Paint mCPaint, mBPaint, mIPaint, mOPaint, mOCPaint;
    private TextPaint mTPaint;
    private float mCenterX, mCenterY, mWidth, mHeight;
    private float mCRadius = 0, mBRadius = 0, mIRadius = 0;
    private float mICenterRadius = 0;
    private float mIDegrees = 0;
    private float mROFill = 0;

    private SpannableStringBuilder mText = new SpannableStringBuilder("");

    private boolean mMirrorIndicator = false;
    private boolean mMirrorRO = false;

    private final float TEXT_REL_RADIUS_WIDTH = 1.8f; //relative
    private final float RADIUS_PADDING = 0.15f; //relative
    private final int CIRCLE_STROKE_WIDTH = 5;

    private Handler mHandler;

    private ReactionActivity mAct;

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mAct = (ReactionActivity) context;

        mHandler = new Handler();

        //colored circle
        mCPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCPaint.setColor(ContextCompat.getColor(getContext(), R.color.pink));
        mCPaint.setStyle(Paint.Style.FILL);

        //colored circle
        mOCPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOCPaint.setColor(ContextCompat.getColor(getContext(), R.color.pink));
        mOCPaint.setStyle(Paint.Style.STROKE);
        mOCPaint.setStrokeJoin(Paint.Join.ROUND);
        mOCPaint.setStrokeCap(Paint.Cap.ROUND);

        //background circle
        mBPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBPaint.setColor(ContextCompat.getColor(getContext(), R.color.dark_nero));
        mBPaint.setStyle(Paint.Style.FILL);

        //indicator
        mIPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIPaint.setColor(ContextCompat.getColor(getContext(), R.color.pink));
        mIPaint.setStyle(Paint.Style.FILL);

        //round text
        mTPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTPaint.setColor(ContextCompat.getColor(getContext(), R.color.snow));
        mTPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.currentRoundTextSize));
        mTPaint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));

        //background overlay circle
        mOPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOPaint.setColor(ContextCompat.getColor(getContext(), R.color.nero));
        mOPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w/2;
        mCenterY = h/2;
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //colored circle
        canvas.drawCircle(mCenterX, mCenterY, mCRadius, mCPaint);
        //background circle
        canvas.drawCircle(mCenterX, mCenterY, mBRadius, mBPaint);

        //task text layout
        if(mTLayout != null) {
            canvas.save();
            updateTLPosition();
            canvas.translate(mTX, mTY);
            mTLayout.draw(canvas);
            canvas.restore();
        }

        //Overlay
        canvas.drawArc(mWidth/2-mBRadius, mHeight/2-mBRadius,
                mWidth - (mWidth/2-mBRadius), mHeight/2+mBRadius,
                mROFill -90 - (mMirrorRO ? 180 : 0), 360-2*(mROFill), false, mOPaint);

        //indicator circle
        float cx = mCenterX + (float)Math.sin(Math.toRadians(mIDegrees))*mICenterRadius;
        float cy = mCenterY - (float)Math.cos(Math.toRadians(mIDegrees))*mICenterRadius;
        canvas.drawCircle(cx, cy, mIRadius, mIPaint);
        if(mMirrorIndicator) {
            cx = mCenterX - (float)Math.sin(Math.toRadians(mIDegrees))*mICenterRadius;
            canvas.drawCircle(cx, cy, mIRadius, mIPaint);
        }
    }

    void blowUp() {
        mIRadius = mCRadius;

        long delay = 0;

        //blow colored circle
        ObjectAnimator blowAnimC = ObjectAnimator.ofFloat(this, "cRadius",
                mCRadius, calcMaxCRadius());
        blowAnimC.setDuration(200);
        blowAnimC.setInterpolator(new OvershootInterpolator());
        blowAnimC.start();

        //blow background circle
        ObjectAnimator blowAnimB = ObjectAnimator.ofFloat(this, "bRadius",
                mCRadius, calcMaxBRadius());
        blowAnimB.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationEnd(Animator animation) {
                mText.replace(0, mText.length(),
                        getContext().getString(R.string.current_round_template, mAct.gameManager.numRound));
                createTextLayout();
            }
            @Override public void onAnimationStart(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        blowAnimB.setDuration(200);
        blowAnimB.setInterpolator(new OvershootInterpolator());
        blowAnimB.start();

        //collapse indicator
        ObjectAnimator collapseIOne = ObjectAnimator.ofFloat(this, "iRadius",
                mIRadius, mIRadius/3);
        collapseIOne.setDuration(220);
        collapseIOne.setInterpolator(new OvershootInterpolator());
        collapseIOne.start();

        //move indicator
        ObjectAnimator moveUpI = ObjectAnimator.ofFloat(this, "iCenterRadius", mICenterRadius,
                calcMaxBRadius()+CIRCLE_STROKE_WIDTH/2);
        moveUpI.setDuration(200);
        moveUpI.setInterpolator(new AccelerateDecelerateInterpolator());
        moveUpI.start();

        delay += 200;

        //move indicator to bottom with split
        setMirrorIndicator(true);
        ObjectAnimator circulateIOne = ObjectAnimator.ofFloat(this, "iDegrees", 0, 360);
        circulateIOne.addUpdateListener(animation -> {
            setBOFill((float)animation.getAnimatedValue() > 180 ? 180 : (float)animation.getAnimatedValue());
        });
        circulateIOne.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationEnd(Animator animation) {
                mMirrorRO = true;
            }
            @Override public void onAnimationStart(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        circulateIOne.setStartDelay(delay);
        circulateIOne.setInterpolator(new AccelerateDecelerateInterpolator());
        circulateIOne.setDuration(1200);
        circulateIOne.start();

        delay += 1200;

        ObjectAnimator circulateITwo = ObjectAnimator.ofFloat(this, "iDegrees", 360, 0);
        circulateITwo.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private boolean first = true;
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if(value > 180) setBOFill(value - 180);
                else if(value > 0) {
                    if(first){
                        // TODO: 08.11.2017 change text and bg color of overlay
                        mText.replace(0, mText.length(), mAct.gameManager.task);
                        mTPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.currentTaskTextSize));
                        createTextLayout();
                        mBPaint.setColor(ContextCompat.getColor(getContext(), R.color.dark_dark_nero));
                        first = false;
                    }
                    setBOFill(180 - value);
                }
            }
        });
        circulateITwo.setStartDelay(delay);
        circulateITwo.setInterpolator(new AccelerateDecelerateInterpolator());
        circulateITwo.setDuration(1200);
        circulateITwo.start();

        delay += 1200;

        //collapse indicator
        ObjectAnimator collapseITwo = ObjectAnimator.ofFloat(this, "iRadius",
                mIRadius/3, 0);
        collapseITwo.setStartDelay(delay);
        collapseITwo.setDuration(200);
        collapseITwo.setInterpolator(new AnticipateInterpolator(2));
        collapseITwo.start();
    }

    private void createTextLayout() {
        mTLWidth = (int)(calcMaxBRadius()* TEXT_REL_RADIUS_WIDTH);
        mTLayout = new DynamicLayout(mText, mTPaint, mTLWidth, Layout.Alignment.ALIGN_CENTER,
                1, 0, false);
    }

    private void updateTLPosition() {
        mTX = mWidth/2 - mTLWidth /2;
        mTY = mCenterY - mTLayout.getHeight()/2;
    }

    private float calcMaxCRadius() {
        return Math.min(mWidth/2 *(1-RADIUS_PADDING), mHeight/2 *(1-RADIUS_PADDING));
    }

    private float calcMaxBRadius() {
        return Math.min(mWidth/2 *(1-RADIUS_PADDING)- CIRCLE_STROKE_WIDTH,
                mHeight/2 *(1-RADIUS_PADDING)- CIRCLE_STROKE_WIDTH);
    }

    public void setCRadius(float cRadius) {
        mCRadius = cRadius;
        invalidate();
    }

    public void setBRadius(float bRadius) {
        mBRadius = bRadius;
        invalidate();
    }

    public void setIRadius(float iRadius) {
        mIRadius = iRadius;
        mOCPaint.setStrokeWidth(iRadius/2);
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

    public void setBOFill(float bOFill) {
        mROFill = bOFill;
        invalidate();
    }

    public void setMirrorIndicator(boolean mirrorIndicator) {
        mMirrorIndicator = mirrorIndicator;
        invalidate();
    }
}
