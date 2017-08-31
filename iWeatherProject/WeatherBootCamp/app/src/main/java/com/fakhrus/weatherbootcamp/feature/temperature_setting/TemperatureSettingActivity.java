package com.fakhrus.weatherbootcamp.feature.temperature_setting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fakhrus.weatherbootcamp.R;
import com.fakhrus.weatherbootcamp.utils.BlurBuilder;
import com.fakhrus.weatherbootcamp.utils.SharedPref;

public class TemperatureSettingActivity extends AppCompatActivity {

    private LinearLayout ll_celcius, ll_fahrenheit, ll_kelvin;
    private ImageView iv_backButton;
    private ImageView iv_blurBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_setting);

        initViews();
        bindViews();
        initBlurBackground();
    }

    private void bindViews() {
        ll_celcius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPref.remove("temperature_type");
                SharedPref.saveString("temperature_type","metric");
                onBackPressed();
                finish();
            }
        });

        ll_fahrenheit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPref.remove("temperature_type");
                SharedPref.saveString("temperature_type", "imperial");
                onBackPressed();
                finish();
            }
        });

        ll_kelvin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPref.remove("temperature_type");
                SharedPref.saveString("temperature_type", "default");
                onBackPressed();
                finish();
            }
        });

        iv_backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    private void initViews() {
        ll_celcius = (LinearLayout) findViewById(R.id.ll_celcius_setting);
        ll_fahrenheit = (LinearLayout) findViewById(R.id.ll_fahrenheit_setting);
        ll_kelvin = (LinearLayout) findViewById(R.id.ll_kelvin_setting);
        iv_backButton = (ImageView) findViewById(R.id.iv_back_button_setting);
        iv_blurBackground = (ImageView) findViewById(R.id.iv_blur_background_setting);
    }

    public void initBlurBackground() {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.city);
        Bitmap blurredBitmap = BlurBuilder.blur(this, bitmap);
        iv_blurBackground.setImageBitmap(blurredBitmap);
    }


}
