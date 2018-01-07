package com.danielweidensdoerfer.reaction;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(android.R.id.content).setBackgroundColor(ContextCompat.getColor(this, R.color.dark_dark_nero));

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.transparent)
                .buttonsColor(R.color.pink)
                .image(R.drawable.intro_img_one)
                .title(getString(R.string.intro_title_one))
                .description(getString(R.string.intro_content_one))
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.transparent)
                .buttonsColor(R.color.pink)
                .image(R.drawable.intro_img_two)
                .title(getString(R.string.intro_title_two))
                .description(getString(R.string.intro_content_two))
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.transparent)
                .buttonsColor(R.color.pink)
                .image(R.drawable.intro_img_three)
                .title(getString(R.string.intro_title_three))
                .description(getString(R.string.intro_content_three))
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.transparent)
                .buttonsColor(R.color.pink)
                .image(R.drawable.intro_img_four)
                .title(getString(R.string.intro_title_four))
                .description(getString(R.string.intro_content_four))
                .build());
    }

    @Override
    protected void onResume() {
        super.onResume();

        //hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
