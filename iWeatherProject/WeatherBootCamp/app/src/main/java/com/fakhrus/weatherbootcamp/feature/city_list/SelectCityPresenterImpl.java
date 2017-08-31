package com.fakhrus.weatherbootcamp.feature.city_list;

import android.util.Log;

import com.fakhrus.weatherbootcamp.BuildConfig;
import com.fakhrus.weatherbootcamp.network.Endpoints;
import com.fakhrus.weatherbootcamp.network.RestClient;
import com.fakhrus.weatherbootcamp.model.WeatherModel;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Fakhrus on 8/22/17.
 */

public class SelectCityPresenterImpl implements SelectCityPresenter {

    SelectCityView selectCityView;

    public SelectCityPresenterImpl(SelectCityView selectCityView) {
        this.selectCityView = selectCityView;
    }

    @Override
    public void networkAddCity(String city, String units) {

        selectCityView.loadingAddWeatherOn();
        selectCityView.showloadingDialog();


        Endpoints api = RestClient.getApiService();

        Call<WeatherModel> addWeatherCall = api.getByCityCountry(city, BuildConfig.API_KEY, units);

        addWeatherCall.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {

                selectCityView.loadingAddWeatherOff();
                selectCityView.dismissloadingDialog();

                if (response.isSuccessful()) {
                    WeatherModel addWeather = response.body();
                    selectCityView.showData(addWeather);

                } else {

                    try {
                        String result = response.errorBody().string();
                        selectCityView.showToast(result);
                        selectCityView.dismissloadingDialog();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
//                selectCityView.showError();
                selectCityView.loadingAddWeatherOff();
                selectCityView.dismissloadingDialog();
                selectCityView.showNoConnectionDialog();
                Log.e("ERROR ", ""+t);
                Log.e("ERROR ", ""+t.getMessage());
                Log.e("ERROR ", ""+t.toString());
                Log.e("ERROR ", ""+call.toString());
                Log.e("ERROR ", ""+call);

            }
        });
    }

    @Override
    public void networkCurrentLocation(String latutude, String longutude, String units) {

        selectCityView.showloadingDialog();

        Endpoints api = RestClient.getApiService();

        Call<WeatherModel> curentLocationCall = api.getByCoordinate(latutude, longutude, BuildConfig.API_KEY, units);

        curentLocationCall.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {

                selectCityView.loadingCurrentLocationOff();
                selectCityView.dismissloadingDialog();

                if (response.isSuccessful()) {
                    WeatherModel currentWeather = response.body();
                    selectCityView.showDataCurentLocation(currentWeather);

                } else {

                    try {
                        String result = response.errorBody().string();
                        selectCityView.showToast(result);
                        selectCityView.dismissloadingDialog();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
//                selectCityView.showError();
                selectCityView.loadingCurrentLocationOff();
                selectCityView.dismissloadingDialog();
                selectCityView.showNoConnectionDialog();

                Log.e("ERROR ", ""+t);
                Log.e("ERROR ", ""+t.getMessage());
                Log.e("ERROR ", ""+t.toString());
                Log.e("ERROR ", ""+call.toString());
                Log.e("ERROR ", ""+call);

            }
        });
    }

}
