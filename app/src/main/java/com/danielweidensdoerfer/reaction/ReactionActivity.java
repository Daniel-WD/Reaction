package com.danielweidensdoerfer.reaction;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.danielweidensdoerfer.reaction.game.GameManager;
import com.danielweidensdoerfer.reaction.game.GameView;
import com.danielweidensdoerfer.reaction.game.GridGenerator;
import com.danielweidensdoerfer.reaction.game.TimerView;
import com.danielweidensdoerfer.reaction.utils.ViewAnimUtils;

public class ReactionActivity extends AppCompatActivity {

    public TextView tvTitle;
    public TextView tvPoints;
    public TextView tvPlayedRounds;
    public TextView tvRoundRecord;
    public TextView tvRemovedObjects;
    public View vStartBg, vDivider;
    public ImageButton btnStart;
    public FrameLayout fStart;
    public LoadingView loadingView;
    public TimerView timerView;
    public GameView gameView;

    public Handler handler = new Handler();

    public GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction);

        gameManager = new GameManager(this);

        tvTitle = findViewById(R.id.tvTitle);
        tvPoints = findViewById(R.id.tvPoints);
        tvPlayedRounds = findViewById(R.id.tvPlayedRounds);
        tvRoundRecord = findViewById(R.id.tvRoundRecord);
        tvRemovedObjects = findViewById(R.id.tvRemovedObjects);
        btnStart = findViewById(R.id.btnStart);
        vDivider = findViewById(R.id.divider);
        vStartBg = findViewById(R.id.startBgView);
        fStart = findViewById(R.id.startFrame);
        loadingView = findViewById(R.id.loadingView);
        timerView = findViewById(R.id.timeView);
        gameView = findViewById(R.id.gameView);

        btnStart.setOnClickListener(v -> {
            gameManager.startGame();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Rotation of start bg
        ObjectAnimator rotation = ObjectAnimator.ofFloat(vStartBg, "rotation", 0, 360);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setRepeatCount(ValueAnimator.INFINITE);
        rotation.setDuration(10000);
        rotation.start();

        //enter animations
        handler.postDelayed(() -> {
            long delay = 100;

            tvTitle.setTranslationY(tvTitle.getHeight()/5);
            tvTitle.setAlpha(0);
            tvTitle.animate()
                    .setStartDelay(delay)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(300)
                    .alpha(1)
                    .translationY(0)
                    .start();

            delay += 200;

            tvPoints.setAlpha(0);
            tvPoints.setTranslationY(tvPoints.getHeight()/3);
            tvPoints.animate()
                    .setStartDelay(delay)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(200)
                    .alpha(1)
                    .translationY(0)
                    .start();

            delay += 100;

            vDivider.setScaleX(0);
            vDivider.animate()
                    .setStartDelay(delay)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(200)
                    .scaleX(1)
                    .start();

            float translateY = -tvPlayedRounds.getHeight()/4;
            delay += 100;
            ViewAnimUtils.translateYAlphaAnimIn(translateY, delay, tvPlayedRounds);
            delay += 100;
            ViewAnimUtils.translateYAlphaAnimIn(translateY, delay, tvRoundRecord);
            delay += 100;
            ViewAnimUtils.translateYAlphaAnimIn(translateY, delay, tvRemovedObjects);
            delay += 300;

            fStart.setAlpha(0);
            fStart.setScaleX(0);
            fStart.setScaleY(0);
            fStart.animate()
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

/*    @Override
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
    }*/
}
