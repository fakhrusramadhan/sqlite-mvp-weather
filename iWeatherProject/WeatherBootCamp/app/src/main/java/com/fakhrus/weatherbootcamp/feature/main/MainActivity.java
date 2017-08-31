package com.fakhrus.weatherbootcamp.feature.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fakhrus.weatherbootcamp.BuildConfig;
import com.fakhrus.weatherbootcamp.feature.city_list.NoConnectionDialog;
import com.fakhrus.weatherbootcamp.feature.city_list.SelectCityActivity;
import com.fakhrus.weatherbootcamp.R;
import com.fakhrus.weatherbootcamp.utils.BlurBuilder;
import com.fakhrus.weatherbootcamp.utils.SharedPref;
import com.fakhrus.weatherbootcamp.model.WeatherModel;


public class MainActivity extends AppCompatActivity implements MainView {

    private int gambarBackground;
    private MainPresenter mainPresenter;
    private String cityName, weather, weatherDescription;
    private double latitude, longitude;
    private String gpsLatitude, gpsLongitude;
    private ImageView iv_background;
    private TextView tv_cityName, tv_weatherDescription, tv_temperature, tv_tempMax, tv_tempMin,
            tv_humidity, tv_windSpeed, tv_pressure;
    private Double windSpeed, pressure;
    private int humidity, temperature, tempMax, tempMin;
    private TextView customTitle;
    private ImageView iv_menuButton, iv_iconWeather, iv_reload;
    private boolean searchByCityName = false;
    private boolean searchByCoordinates = false;
    private LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);
    private NoConnectionDialog noConnectionDialog = new NoConnectionDialog(MainActivity.this);
    private String temperatureType;
    private String temperatureSymbol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSharedPref();
        initPresenter();
        callPresenter();
        initViews();
        bindViews();

    }

    private void initPresenter() {
        mainPresenter = new MainPresenterImpl(this);
    }

    private void initSharedPref() {

        if (SharedPref.check("temperature_type")){
            temperatureType = SharedPref.getString("temperature_type");
            if (temperatureType.equals("metric")){
                temperatureSymbol = "°C";
            }else if (temperatureType.equals("imperial")){
                temperatureSymbol = "°F";
            }else {
                temperatureSymbol = "°K";
            }
        }

        if (SharedPref.check("city_name")) {
            cityName = SharedPref.getString("city_name");
            searchByCityName = true;
        }

        if (SharedPref.check("gps_latitude") && SharedPref.check("gps_longitude")) {
            gpsLatitude = SharedPref.getString("gps_latitude");
            gpsLongitude = SharedPref.getString("gps_longitude");
            searchByCoordinates = true;
        }
    }


    private void bindViews() {
        iv_menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent toSelectCity = new Intent(MainActivity.this, SelectCityActivity.class);
                toSelectCity.putExtra("weatherCondition", weather);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(toSelectCity);
                finish();
            }
        });

        iv_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSharedPref();
                callPresenter();
            }
        });
    }

    private void initViews() {
        iv_background = (ImageView) findViewById(R.id.iv_blur_background_main);
        tv_cityName = (TextView) findViewById(R.id.tv_city_main);
        tv_weatherDescription = (TextView) findViewById(R.id.tv_weather_type_main);
        tv_temperature = (TextView) findViewById(R.id.tv_temperature_main);
        tv_tempMax = (TextView) findViewById(R.id.tv_max_temp_main);
        tv_tempMin = (TextView) findViewById(R.id.tv_min_temp_main);
        tv_humidity = (TextView) findViewById(R.id.tv_humidity_main);
        tv_windSpeed = (TextView) findViewById(R.id.tv_wind_speed_main);
        tv_pressure = (TextView) findViewById(R.id.tv_pressure_main);
        iv_menuButton = (ImageView) findViewById(R.id.iv_menu_button_main);
        iv_iconWeather = (ImageView) findViewById(R.id.iv_icon_weather_main);
        iv_reload = (ImageView) findViewById(R.id.iv_refresh_button_main);

    }

    private void callPresenter() {

        if (searchByCityName) {
            mainPresenter.initMainWeatherNetwork(cityName, temperatureType, BuildConfig.API_KEY);
            HorizontalWeatherFragment weatherFragment = new HorizontalWeatherFragment(cityName);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_horizontal_weather_main, weatherFragment).commit();
            searchByCityName = false;

        } else if (searchByCoordinates) {

            mainPresenter.initMainGPSWeatherNetwork(gpsLatitude, gpsLongitude, temperatureType, BuildConfig.API_KEY);
            HorizontalWeatherFragment weatherFragment = new HorizontalWeatherFragment(gpsLatitude, gpsLongitude);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_horizontal_weather_main, weatherFragment).commit();
            searchByCoordinates = false;
        }

    }


    @Override
    public void showData(WeatherModel mainWeather) {
        cityName = mainWeather.getName();
        latitude = mainWeather.getCoord().getLat();
        longitude = mainWeather.getCoord().getLon();
        temperature = mainWeather.getMain().getTemp();
        tempMax = mainWeather.getMain().getTempMax();
        tempMin = mainWeather.getMain().getTempMin();
        pressure = mainWeather.getMain().getPressure();
        windSpeed = mainWeather.getWind().getSpeed();
        weather = mainWeather.getWeather().get(0).getMain();
        weatherDescription = mainWeather.getWeather().get(0).getDescription();
        humidity = mainWeather.getMain().getHumidity();

        Log.e("WEATHER ", "" + weather);

        tv_cityName.setText(cityName);
        tv_temperature.setText(temperature + temperatureSymbol);
        tv_tempMax.setText(tempMax + temperatureSymbol);
        tv_tempMin.setText(tempMin + temperatureSymbol);
        tv_weatherDescription.setText(weatherDescription);
        tv_humidity.setText("" + humidity + " %");
        tv_windSpeed.setText("" + windSpeed + " km/h");
        tv_pressure.setText(pressure + " mmHg");


    }

    @Override
    public void showError() {
        Toast.makeText(this, "PERIKSA INTERNET ANDA", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToast(String data) {
        Toast.makeText(this, "DATA : " + data, Toast.LENGTH_LONG).show();
    }


    @Override
    public void initBlurBackground() {

        if (weather.equals("Clear")) {
            gambarBackground = R.drawable.sunny;
            iv_iconWeather.setImageResource(R.drawable.ic_sunny);
        } else if (weather.equals("Clouds")) {
            gambarBackground = R.drawable.clouds;
            iv_iconWeather.setImageResource(R.drawable.ic_cloudy);
        } else if (weather.equals("Rain")) {
            gambarBackground = R.drawable.rainy;
            iv_iconWeather.setImageResource(R.drawable.ic_rainy);
        } else if (weather.equals("Haze") || weather.equals("Mist")) {
            gambarBackground = R.drawable.fog;
            iv_iconWeather.setImageResource(R.drawable.ic_fog);
        } else {
            gambarBackground = R.drawable.other;
            iv_iconWeather.setImageResource(R.drawable.ic_cloudy_sunny);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), gambarBackground);
        Bitmap blurredBitmap = BlurBuilder.blur(this, bitmap);
        iv_background.setImageBitmap(blurredBitmap);
    }

    @Override
    public void showLoadingContent() {
        loadingDialog.showLoadingDialog();
    }

    @Override
    public void dismissLoadingContent() {
        loadingDialog.dismissLoadingDialog();
    }

    @Override
    public void showNoConnectionDialog() {
        noConnectionDialog.showNoConnectionDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (searchByCityName) {
            mainPresenter.initMainWeatherNetwork(cityName, temperatureType, BuildConfig.API_KEY);
            searchByCityName = false;

        } else if (searchByCoordinates) {

            mainPresenter.initMainGPSWeatherNetwork(gpsLatitude, gpsLongitude, temperatureType, BuildConfig.API_KEY);
            searchByCoordinates = false;
        }

    }

}
