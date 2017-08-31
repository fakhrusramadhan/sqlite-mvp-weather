package com.fakhrus.weatherbootcamp.feature.splash_screen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.fakhrus.weatherbootcamp.feature.intro.IntroActivity;
import com.fakhrus.weatherbootcamp.feature.main.MainActivity;
import com.fakhrus.weatherbootcamp.R;
import com.fakhrus.weatherbootcamp.utils.BlurBuilder;
import com.fakhrus.weatherbootcamp.utils.SharedPref;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView iv_background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initView();
        initBlurBackground();
        initTimer();
    }

    private void initTimer() {

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    // 3000 = 3 detik
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {


                    if (SharedPref.getBoolean("weather_added")){

                        Intent toMain = new Intent(SplashScreenActivity.this, MainActivity.class);
                        toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(toMain);

                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    }else {


                        Intent toIntro = new Intent(SplashScreenActivity.this, IntroActivity.class);
                        toIntro.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(toIntro);

                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    }

                }
            }
        };

        timerThread.start();

    }

    private void initView() {
        iv_background = (ImageView) findViewById(R.id.iv_blur_background_splash);
    }

    private void initBlurBackground() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.splash);
        Bitmap blurredBitmap = BlurBuilder.blur(this, bitmap);
        iv_background.setImageBitmap(blurredBitmap);
    }
}
