package com.fakhrus.weatherbootcamp.feature.main;

import android.util.Log;

import com.fakhrus.weatherbootcamp.network.Endpoints;
import com.fakhrus.weatherbootcamp.network.RestClient;
import com.fakhrus.weatherbootcamp.model.WeatherModel;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Fakhrus on 8/21/17.
 */

public class MainPresenterImpl implements MainPresenter {

    private MainView mainView;

    public MainPresenterImpl(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void initMainWeatherNetwork(String city, String temperatureType, String API_KEY) {

        mainView.showLoadingContent();

        Endpoints api = RestClient.getApiService();

        Call<WeatherModel> mainWeatherCall = api.getByCityCountry(city, API_KEY, temperatureType);

        mainWeatherCall.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {

                mainView.dismissLoadingContent();

                if (response.isSuccessful()) {

                    WeatherModel mainWeather = response.body();
                    mainView.showData(mainWeather);
                    mainView.initBlurBackground();

                } else {

                    try {
                        String result = response.errorBody().string();
                        mainView.showToast(result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
//                mainView.showError();
                mainView.dismissLoadingContent();
                mainView.showNoConnectionDialog();

                Log.e("ERROR ", "" + t);
                Log.e("ERROR ", "" + t.getMessage());
                Log.e("ERROR ", "" + t.toString());
                Log.e("ERROR ", "" + call.toString());
                Log.e("ERROR ", "" + call);

            }
        });

    }

    @Override
    public void initMainGPSWeatherNetwork(String latitude, String longitude, String temperatureType, String API_KEY) {

        mainView.showLoadingContent();

        Endpoints api = RestClient.getApiService();

        Call<WeatherModel> mainWeatherCall = api.getByCoordinate(latitude, longitude, API_KEY, temperatureType);

        mainWeatherCall.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {

                mainView.dismissLoadingContent();

                if (response.isSuccessful()) {

                    WeatherModel mainWeather = response.body();
                    mainView.showData(mainWeather);
                    mainView.initBlurBackground();

                } else {

                    try {
                        String result = response.errorBody().string();
                        mainView.showToast(result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
//                mainView.showError();
                mainView.dismissLoadingContent();
                mainView.showNoConnectionDialog();

                Log.e("ERROR ", "" + t);
                Log.e("ERROR ", "" + t.getMessage());
                Log.e("ERROR ", "" + t.toString());
                Log.e("ERROR ", "" + call.toString());
                Log.e("ERROR ", "" + call);

            }
        });

    }
}
