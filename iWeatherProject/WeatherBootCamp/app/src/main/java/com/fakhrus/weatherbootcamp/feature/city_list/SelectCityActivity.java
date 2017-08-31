package com.fakhrus.weatherbootcamp.feature.city_list;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fakhrus.weatherbootcamp.R;
import com.fakhrus.weatherbootcamp.feature.main.LoadingDialog;
import com.fakhrus.weatherbootcamp.feature.main.MainActivity;
import com.fakhrus.weatherbootcamp.feature.temperature_setting.TemperatureSettingActivity;
import com.fakhrus.weatherbootcamp.utils.BlurBuilder;
import com.fakhrus.weatherbootcamp.utils.GPSService;
import com.fakhrus.weatherbootcamp.utils.SQLiteHandler;
import com.fakhrus.weatherbootcamp.model.ItemOfWeatherModel;
import com.fakhrus.weatherbootcamp.model.WeatherModel;
import com.fakhrus.weatherbootcamp.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;

public class SelectCityActivity extends AppCompatActivity implements SelectCityView {

    private LinearLayout ll_addWeather, ll_currentLocation;
    private RecyclerView rv_listCity;
    private SelectCityPresenter selectCityPresenter;
    private String cityName;
    private int temperature;
    private SelectCityAdapter adapter;

    private List<ItemOfWeatherModel> itemOfWeather = new ArrayList<>();
    private ImageView iv_backgroundSelectCity, iv_settingButton;

    private ProgressBar pb_currentLocation, pb_addWeather;

    public static final int RequestPermissionCode = 1;
    private SQLiteHandler db;
    private String weatherType, weatherDescription;

    private BroadcastReceiver broadcastReceiver;

    private LoadingDialog loadingDialog = new LoadingDialog(SelectCityActivity.this);
    private NoConnectionDialog noConnectionDialog = new NoConnectionDialog(SelectCityActivity.this);
    private String temperatureType, temperatureSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        db = new SQLiteHandler(this);

        initSharedPref();
        initView();
        callAllWeatherSQLite();
        initPresenter();
        EnableRuntimePermission();

        initBlurBackground();
        bindView();

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
    }

    private void callAllWeatherSQLite() {
        itemOfWeather = db.getAllItemWeather();
        initRecyclerView();
    }


    private void bindView() {
        ll_addWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCity();
            }
        });

        ll_currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GPSService.class);
                startService(i);

                loadingCurrentLocationOn();
                showloadingDialog();
            }
        });

        iv_settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSetting = new Intent(SelectCityActivity.this, TemperatureSettingActivity.class);
                startActivity(toSetting);
            }
        });
    }

    public void addCity() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Tambah Cuaca");

        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setHint("masukkan nama kota...");
        input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        initSharedPref();
                        cityName = input.getText().toString();
                        selectCityPresenter.networkAddCity(cityName, temperatureType);
                    }
                });
        alertDialog.setNegativeButton("Batal",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private void initPresenter() {
        selectCityPresenter = new SelectCityPresenterImpl(SelectCityActivity.this);
    }

    private void initView() {
        ll_addWeather = (LinearLayout) findViewById(R.id.ll_add_weather);
        ll_currentLocation = (LinearLayout) findViewById(R.id.ll_current_location);
        pb_currentLocation = (ProgressBar) findViewById(R.id.pb_current_location_progress);
        pb_addWeather = (ProgressBar) findViewById(R.id.pb_add_weather_progress);
        rv_listCity = (RecyclerView) findViewById(R.id.rv_list_city_selectCity);
        iv_backgroundSelectCity = (ImageView) findViewById(R.id.iv_blur_background_selectCity);
        iv_settingButton = (ImageView) findViewById(R.id.iv_setting_button_selectCity);
    }

    @Override
    public void showData(WeatherModel addWeather) {

        cityName = addWeather.getName();
        temperature = addWeather.getMain().getTemp();
        weatherType = addWeather.getWeather().get(0).getMain();
        weatherDescription = addWeather.getWeather().get(0).getDescription();

        if (!cityName.isEmpty()) {

            ItemOfWeatherModel weather = new ItemOfWeatherModel();
            weather.setCityName(cityName);
            weather.setTemperature(temperature+temperatureSymbol);
            weather.setWeatherType(weatherType);
            weather.setWeatherDescription(weatherDescription);
            itemOfWeather.add(weather);

            saveWeatherData(weather);

        }

        initRecyclerView();
    }

    private void initRecyclerView() {

        adapter = new SelectCityAdapter(this, itemOfWeather);
        rv_listCity.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SelectCityActivity.this);
        rv_listCity.setLayoutManager(layoutManager);
        rv_listCity.setHasFixedSize(true);
    }

    private void saveWeatherData(ItemOfWeatherModel itemOfWeatherModel) {
        Log.d("Insert: ", "Inserting ..");
        db.addWeather(itemOfWeatherModel);
    }

    @Override
    public void showError() {
        Toast.makeText(this, "PERIKSA INTERNET ANDA", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToast(String data) {
        Toast.makeText(this, "Info : " + data, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDataCurentLocation(WeatherModel currentLocation) {
        cityName = currentLocation.getName();
        temperature = currentLocation.getMain().getTemp();

        if (!cityName.isEmpty()) {
            ItemOfWeatherModel weather = new ItemOfWeatherModel();
            weather.setCityName(cityName);
            weather.setTemperature(temperature+temperatureSymbol);
            weather.setWeatherType(currentLocation.getWeather().get(0).getMain());
            weather.setWeatherDescription(currentLocation.getWeather().get(0).getDescription());
            itemOfWeather.add(weather);
        }

        adapter = new SelectCityAdapter(this, itemOfWeather);
        rv_listCity.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SelectCityActivity.this);
        rv_listCity.setLayoutManager(layoutManager);
        rv_listCity.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadingAddWeatherOn() {
        ll_addWeather.setVisibility(View.GONE);
        pb_addWeather.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadingAddWeatherOff() {
        ll_addWeather.setVisibility(View.VISIBLE);
        pb_addWeather.setVisibility(View.GONE);
    }

    @Override
    public void loadingCurrentLocationOn() {
        ll_currentLocation.setVisibility(View.GONE);
        pb_currentLocation.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadingCurrentLocationOff() {
        ll_currentLocation.setVisibility(View.GONE);
        pb_currentLocation.setVisibility(View.VISIBLE);
    }

    @Override
    public void showloadingDialog() {
        loadingDialog.showLoadingDialog();
    }

    @Override
    public void dismissloadingDialog() {
        loadingDialog.dismissLoadingDialog();
    }

    @Override
    public void showNoConnectionDialog() {
        noConnectionDialog.showNoConnectionDialog();
    }



    public void initBlurBackground() {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.city);
        Bitmap blurredBitmap = BlurBuilder.blur(this, bitmap);
        iv_backgroundSelectCity.setImageBitmap(blurredBitmap);
    }


    public void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(SelectCityActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(SelectCityActivity.this, "ACCESS_FINE_LOCATION permission allows us to Access GPS in app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(SelectCityActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {
            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(SelectCityActivity.this, "Permission Granted, Now your application can access GPS.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SelectCityActivity.this, "Izin pemakaian GPS pernah anda tolak,\nAplikasi tidak dapat menjalankan GPS.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        callAllWeatherSQLite();

        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    String latitude = "" + intent.getStringExtra("coordinate_latitude");
                    String longitude = "" + intent.getStringExtra("coordinate_longitude");

//                    Toast.makeText(SelectCityActivity.this, "lat " + latitude + "\nlon " + longitude, Toast.LENGTH_LONG).show();

                    SharedPref.remove("city_name");
                    SharedPref.remove("gps_latitude");
                    SharedPref.remove("gps_longitude");
                    saveLatLong(latitude, longitude);

                    dismissloadingDialog();

                    Intent toMain = new Intent(SelectCityActivity.this, MainActivity.class);
                    toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    startActivity(toMain);
                    finish();

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

}
