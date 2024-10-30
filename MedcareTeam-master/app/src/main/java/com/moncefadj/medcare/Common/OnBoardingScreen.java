package com.moncefadj.medcare.Common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moncefadj.medcare.HelperClasses.SlidesAdapter;
import com.moncefadj.medcare.R;

public class OnBoardingScreen extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;
    LinearLayout buttonsLayout;
    TextView[] dots;
    Button skipBtn,nextBtn,startBtn;

    int currentSlide;

    SlidesAdapter slidesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding_screen);


        //Hooks
        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.dots_layout);
        buttonsLayout = findViewById(R.id.buttons_layout);
        skipBtn = findViewById(R.id.skip_btn);
        nextBtn = findViewById(R.id.next_btn);
        startBtn = findViewById(R.id.lets_get_started_btn);

        slidesAdapter = new SlidesAdapter(this);
        viewPager.setAdapter(slidesAdapter);

        addDots(0);

        viewPager.addOnPageChangeListener(changeListener);


    }

    public void skip(View view) {
        Intent intent = new Intent(OnBoardingScreen.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void next(View view) {
        viewPager.setCurrentItem(currentSlide + 1);
    }

    public void start(View view) {
        Intent intent = new Intent(OnBoardingScreen.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void addDots(int pos) {
        dots = new TextView[3];

        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(40);

            dotsLayout.addView(dots[i]);

        }

        if (dots.length > 0) {
            dots[pos].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }


    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            currentSlide = position;

            Animation fadeIn = AnimationUtils.loadAnimation(OnBoardingScreen.this,R.anim.fade_in);
            Animation fadeOut = AnimationUtils.loadAnimation(OnBoardingScreen.this,R.anim.fade_out);

            if (position == 2) { // the last slide


                skipBtn.startAnimation(fadeOut);
                nextBtn.startAnimation(fadeOut);
                startBtn.startAnimation(fadeIn);

                skipBtn.setVisibility(View.GONE);
                nextBtn.setVisibility(View.GONE);
                startBtn.setVisibility(View.VISIBLE);

            } else {

                skipBtn.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
                startBtn.setVisibility(View.GONE);
            }

            addDots(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}