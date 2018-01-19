package com.develdaniel.reaction;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.develdaniel.reaction.bases.Colorbase;
import com.develdaniel.reaction.bases.Database;
import com.develdaniel.reaction.game.CrossView;
import com.develdaniel.reaction.game.GameBackground;
import com.develdaniel.reaction.game.GameManager;
import com.develdaniel.reaction.game.GameView;
import com.develdaniel.reaction.bases.itembase.Itembase;
import com.develdaniel.reaction.game.generator.GridGenerator;
import com.develdaniel.reaction.helper.ReflectingLayout;
import com.develdaniel.reaction.loading.LoadingView;
import com.develdaniel.reaction.game.TimerView;
import com.develdaniel.reaction.utils.ViewAnimUtils;

public class ReactionActivity extends AppCompatActivity {

    private static int RQ_INTRO_ACTIVITY = 1;

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
    public GameBackground gameBackground;
    public CrossView crossView;
    public TextView tvGamePoints;
    public ImageView ivCrossTick;
    public TextView tvNewGamePoints;
    public TextView tvTimeGamePoints;
    public TextView tvBonus;
    public ImageButton btnRepeat;
    public TextView tvLoose;
    public ImageButton btnHome;
    public ReflectingLayout reflectingLayout;
    //public View resultBackground;
    //public TextView tvTask;

    public Handler handler = new Handler();

    public GameManager gameManager;

    private static boolean mStarted = false;

    public int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction);

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        width = size.x;
        height = size.y;

        int count = 8;
        float[] c = {0, 0.88f, 0.76f};

        for(int i = 0; i < count; i++) {
            c[0] = 360/count * i;
            int color = Color.HSVToColor(c);
            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);
            String red = Integer.toString(r, 16);
            String green = Integer.toString(g, 16);
            String blue = Integer.toString(b, 16);
            Log.d("TAG", "<color name=\"_" + String.valueOf(i+1) + "\">#" + red + green + blue + "</color>");
        }

        Database.init(this);
        Colorbase.init(this);
        Itembase.init(this);

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
        gameBackground = findViewById(R.id.gameBackground);
        crossView = findViewById(R.id.crossView);
        tvGamePoints = findViewById(R.id.tvGamePoints);
        ivCrossTick = findViewById(R.id.ivCrossTick);
        tvNewGamePoints = findViewById(R.id.tvNewGamePoints);
        tvTimeGamePoints = findViewById(R.id.tvTimeGamePoints);
        tvBonus = findViewById(R.id.tvBonus);
        btnRepeat = findViewById(R.id.btnRepeat);
        tvLoose = findViewById(R.id.tvLoose);
        btnHome = findViewById(R.id.btnHome);
        reflectingLayout = findViewById(R.id.reflectionLayout);
//        resultBackground = findViewById(R.id.resultBackground);
        //tvTask = findViewById(R.id.tvTask);

        gameView.post(() -> GridGenerator.init(this));

        btnHome.setOnClickListener(v -> {
            updateValues();
            gameManager.closeLooseScreen(false);
        });

        btnStart.setOnClickListener(v -> {
            gameManager.startGame();
        });

        btnRepeat.setOnClickListener(v -> {
            gameManager.closeLooseScreen(true);
        });

        prepareEnterAnim();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Rotation of start button background
        ObjectAnimator rotation = ObjectAnimator.ofFloat(vStartBg, "rotation", 0, 360);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setRepeatCount(ValueAnimator.INFINITE);
        rotation.setDuration(15000);
        rotation.start();

        //enter animations
        if(!mStarted) handler.postDelayed(this::startEnterAnim, 100);

        mStarted = true;
    }

    private void prepareEnterAnim() {
        reflectingLayout.setAlpha(0);
        tvPoints.setAlpha(0);
        tvPlayedRounds.setAlpha(0);
        tvRoundRecord.setAlpha(0);
        tvRemovedObjects.setAlpha(0);
        fStart.setAlpha(0);
        vDivider.setScaleX(0);
    }

    private void startEnterAnim() {
        long delay = 100;

        reflectingLayout.setTranslationY(tvTitle.getHeight()/3);
        reflectingLayout.setAlpha(0);
        reflectingLayout.animate()
                .setStartDelay(delay)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(1500)
                .alpha(1)
                .translationY(0)
                .start();

        delay += 500;

        tvPoints.setAlpha(0);
        tvPoints.setTranslationY(-tvPoints.getHeight());
        tvPoints.animate()
                .setStartDelay(delay)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(300)
                .alpha(1)
                .translationY(0)
                .start();

        delay += 200;

        vDivider.setScaleY(0);
        vDivider.setScaleX(0);
        vDivider.animate()
                .setStartDelay(delay)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(300)
                .scaleX(1)
                .scaleY(1)
                .start();

        delay += 200;
        float translateY = tvPlayedRounds.getHeight();
        ViewAnimUtils.translateYAlphaAnimIn(translateY, delay, 300, tvPlayedRounds);
        delay += 50;
        ViewAnimUtils.translateYAlphaAnimIn(translateY, delay, 300, tvRoundRecord);
        delay += 50;
        ViewAnimUtils.translateYAlphaAnimIn(translateY, delay, 300, tvRemovedObjects);
        delay += 400;

        float tY = height - fStart.getY();
        fStart.setAlpha(0);
        fStart.setTranslationY(tY);
        fStart.animate()
                .setDuration(800)
                .setStartDelay(delay)
                .setInterpolator(new OvershootInterpolator(2F))
                .alpha(1)
                .translationY(1)
                .start();
    }

    public void updateValues() {
        tvPoints.setText(getString(R.string.record_points_template, Database.recordPoints));
        tvPlayedRounds.setText(getString(R.string.played_rounds_template, Database.playedRounds));
        tvRoundRecord.setText(getString(R.string.round_record_template, Database.roundRecord));
        tvRemovedObjects.setText(getString(R.string.removed_objects_template, Database.removedObjects));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Database.load();
        updateValues();

        if(Database.firstStart) {
            Database.firstStart = false;
            prepareEnterAnim();
            Intent intent = new Intent(getBaseContext(), IntroActivity.class);
            startActivityForResult(intent, RQ_INTRO_ACTIVITY);
        }

        //hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RQ_INTRO_ACTIVITY) {
            startEnterAnim();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Database.save();
    }

    @Override
    public void onBackPressed() {
        if(gameManager.isInLooseScreen) btnHome.callOnClick();
        if(reflectingLayout.getVisibility() == View.VISIBLE) super.onBackPressed();
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
