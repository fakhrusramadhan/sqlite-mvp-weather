package com.fakhrus.weatherbootcamp.feature.intro;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fakhrus.weatherbootcamp.feature.city_list.NoConnectionDialog;
import com.fakhrus.weatherbootcamp.feature.main.LoadingDialog;
import com.fakhrus.weatherbootcamp.utils.GPSService;
import com.fakhrus.weatherbootcamp.feature.main.MainActivity;
import com.fakhrus.weatherbootcamp.R;
import com.fakhrus.weatherbootcamp.utils.BlurBuilder;
import com.fakhrus.weatherbootcamp.utils.SharedPref;

public class IntroActivity extends AppCompatActivity implements IntroView {

    private TextView et_cityName;
    private Button bt_ok;
    private Button bt_currentLocation;
    private ImageView iv_background;

    private BroadcastReceiver broadcastReceiver;
    private TextView textView;

    private LoadingDialog loadingDialog = new LoadingDialog(IntroActivity.this);
    private TextView tv_celcius, tv_fahrenheit, tv_kelvin;


    @Override
    protected void onResume() {
        super.onResume();

        if (!runtimePermissions()){
            buttonCanBeClicked();
        }

        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    String latitude = ""+intent.getStringExtra("coordinate_latitude");
                    String longitude = ""+intent.getStringExtra("coordinate_longitude");

                    saveLatLong(latitude, longitude);

                    SharedPref.saveBoolean("weather_added", true);

                    dismissLoadingDialog();

                    Intent toMain = new Intent(IntroActivity.this, MainActivity.class);
                    toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(toMain);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                    // ===== STOP GPS SERVICE ======
                    Intent i = new Intent(getApplicationContext(), GPSService.class);
                    stopService(i);

                }
            };
        }
        //broadcast receiver
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    private void saveLatLong(String latitude, String longitude) {
        SharedPref.saveString("gps_latitude", latitude);
        SharedPref.saveString("gps_longitude", longitude);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        initViews();
        bindViews();
        initSharedPref();
        initBlurBackground();

        if (!runtimePermissions()){
            buttonCanBeClicked();
        }


    }

    private void initSharedPref() {
        SharedPref.saveString("temperature_type", "metric");
        tv_celcius.setBackgroundResource(R.drawable.rounded_border_button_blue);
    }

    private void initBlurBackground() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.city);
        Bitmap blurredBitmap = BlurBuilder.blur(this, bitmap);
        iv_background.setImageBitmap(blurredBitmap);
    }

    private void buttonCanBeClicked() {

        bt_currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), GPSService.class);
                startService(i);

                showLoadingDialog();

//                Toast.makeText(IntroActivity.this, "Panggil Service GPS ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void bindViews() {

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPref.saveBoolean("weather_added", true);
                SharedPref.saveString("city_name", getCityName());

                Intent toMain = new Intent(IntroActivity.this, MainActivity.class);
                toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(toMain);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        tv_celcius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPref.remove("temperature_type");
                SharedPref.saveString("temperature_type", "metric");
                tv_celcius.setBackgroundResource(R.drawable.rounded_border_button_blue);
                tv_fahrenheit.setBackgroundResource(R.drawable.rounded_border_button);
                tv_kelvin.setBackgroundResource(R.drawable.rounded_border_button);
            }
        });

        tv_fahrenheit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPref.remove("temperature_type");
                SharedPref.saveString("temperature_type", "imperial");
                tv_fahrenheit.setBackgroundResource(R.drawable.rounded_border_button_blue);
                tv_celcius.setBackgroundResource(R.drawable.rounded_border_button);
                tv_kelvin.setBackgroundResource(R.drawable.rounded_border_button);
            }
        });

        tv_kelvin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPref.remove("temperature_type");
                SharedPref.saveString("temperature_type", "default");
                tv_kelvin.setBackgroundResource(R.drawable.rounded_border_button_blue);
                tv_fahrenheit.setBackgroundResource(R.drawable.rounded_border_button);
                tv_celcius.setBackgroundResource(R.drawable.rounded_border_button);
            }
        });

    }

    private void initViews() {
        et_cityName = (TextView) findViewById(R.id.et_city_name_intro);
        bt_ok = (Button) findViewById(R.id.bt_ok_intro);
        bt_currentLocation = (Button) findViewById(R.id.bt_current_location_intro);
        iv_background = (ImageView) findViewById(R.id.iv_blur_background_intro);
        tv_celcius = (TextView) findViewById(R.id.tv_celcius_type);
        tv_fahrenheit = (TextView) findViewById(R.id.tv_fahrenheit_type);
        tv_kelvin = (TextView) findViewById(R.id.tv_kelvin_type);
        textView = (TextView) findViewById(R.id.tv_gps);
    }

    @Override
    public String getCityName() {
        return et_cityName.getText().toString();
    }

    @Override
    public void showLoadingDialog() {
        loadingDialog.showLoadingDialog();
    }

    @Override
    public void dismissLoadingDialog() {
        loadingDialog.dismissLoadingDialog();
    }



    private boolean runtimePermissions() {

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                buttonCanBeClicked();
            } else {
                runtimePermissions();
            }
        }
    }



}
