package danielweidensdoerfer.com.reaction;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class ReactionActivity extends AppCompatActivity {

    private TextView mTvTitle;
    private TextView mTvPoints;
    private TextView mTvPlayedRounds;
    private TextView mTvRoundRecord;
    private TextView mTvRemovedObjects;
    private View mVStartBg, mVDivider;
    private ImageButton mBtnStart;
    private FrameLayout mStartFrame;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction);

        mTvTitle = findViewById(R.id.tvTitle);
        mTvPoints = findViewById(R.id.tvPoints);
        mTvPlayedRounds = findViewById(R.id.tvPlayedRounds);
        mTvRoundRecord = findViewById(R.id.tvRoundRecord);
        mTvRemovedObjects = findViewById(R.id.tvRemovedObjects);
        mBtnStart = findViewById(R.id.btnStart);
        mVDivider = findViewById(R.id.divider);
        mVStartBg = findViewById(R.id.startBgView);
        mStartFrame = findViewById(R.id.startFrame);

        mBtnStart.setOnClickListener(v -> {

            long delay = 0;
            mVStartBg.animate()
                    .setDuration(300)
                    .setInterpolator(new AnticipateInterpolator(4))
                    .scaleX(0)
                    .scaleY(0)
                    .start();

            delay += 200;

            mBtnStart.animate()
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.play_to_circle, getTheme());
                            mBtnStart.setImageDrawable(drawable);
                            drawable.start();
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .setStartDelay(delay)
                    .setDuration(300)
                    .setInterpolator(new OvershootInterpolator(5))
                    .scaleX(1.5f)
                    .scaleY(1.5f)
                    .start();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Rotation of start bg
        ObjectAnimator rotation = ObjectAnimator.ofFloat(mVStartBg, "rotation", 0, 360);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setRepeatCount(ValueAnimator.INFINITE);
        rotation.setDuration(10000);
        rotation.start();

        //enter animations
        mHandler.postDelayed(() -> {
            long delay = 100;

            mTvTitle.setTranslationY(mTvTitle.getHeight()/5);
            mTvTitle.setAlpha(0);
            mTvTitle.animate()
                    .setStartDelay(delay)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(300)
                    .alpha(1)
                    .translationY(0)
                    .start();

            delay += 200;

            mTvPoints.setAlpha(0);
            mTvPoints.setTranslationY(mTvPoints.getHeight()/3);
            mTvPoints.animate()
                    .setStartDelay(delay)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(200)
                    .alpha(1)
                    .translationY(0)
                    .start();

            delay += 100;

            mVDivider.setScaleX(0);
            mVDivider.animate()
                    .setStartDelay(delay)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(200)
                    .scaleX(1)
                    .start();

            delay += 100;
            ViewAnimUtils.translateYAlphaAnim(mTvPlayedRounds, delay);
            delay += 100;
            ViewAnimUtils.translateYAlphaAnim(mTvRoundRecord, delay);
            delay += 100;
            ViewAnimUtils.translateYAlphaAnim(mTvRemovedObjects, delay);
            delay += 300;

            mStartFrame.setAlpha(0);
            mStartFrame.setScaleX(0);
            mStartFrame.setScaleY(0);
            mStartFrame.animate()
                    .setDuration(300)
                    .setStartDelay(delay)
                    .setInterpolator(new OvershootInterpolator(3F))
                    .scaleX(1)
                    .scaleY(1)
                    .alpha(1)
                    .start();
        }, 100);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.actionSettings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
