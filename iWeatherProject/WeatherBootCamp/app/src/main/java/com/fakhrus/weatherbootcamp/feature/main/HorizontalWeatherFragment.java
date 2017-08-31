package com.fakhrus.weatherbootcamp.feature.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fakhrus.weatherbootcamp.BuildConfig;
import com.fakhrus.weatherbootcamp.network.Endpoints;
import com.fakhrus.weatherbootcamp.network.RestClient;
import com.fakhrus.weatherbootcamp.R;
import com.fakhrus.weatherbootcamp.model.WeatherFiveDaysModel;
import com.fakhrus.weatherbootcamp.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Fakhrus on 8/22/17.
 */

public class HorizontalWeatherFragment extends Fragment {

    List<WeatherFiveDaysModel.ListData> itemHorizontalWeather = new ArrayList<>();
    RecyclerView rv_horizontalList;

    String cityName, latitude, longitude;
    boolean city = false;
    boolean currentLocation = false;
    private String temperatureType, temperatureSymbol;

    public HorizontalWeatherFragment(String cityName) {
        this.cityName = cityName;
        city = true;
    }

    public HorizontalWeatherFragment(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        currentLocation = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewHorizontalListView = inflater.inflate(R.layout.fragment_horizontal_weather, null);

        initSharedPref();

        if (city){
            initNetworkWeatherFiveDays(viewHorizontalListView);
            city = false;
        }else if (currentLocation){
            initNetworkWeatherByGPS(viewHorizontalListView);
            currentLocation = false;
        }

        return viewHorizontalListView;

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

    private void initNetworkWeatherByGPS(final View viewHorizontalListView) {

        Endpoints api = RestClient.getApiService();

        Call<WeatherFiveDaysModel> mainWeatherCall = api.getByCityCountryFiveDaysByGPS(latitude, longitude, BuildConfig.API_KEY, temperatureType);

        mainWeatherCall.enqueue(new Callback<WeatherFiveDaysModel>() {
            @Override
            public void onResponse(Call<WeatherFiveDaysModel> call, Response<WeatherFiveDaysModel> response) {

                itemHorizontalWeather = response.body().getList();

                if (response.isSuccessful()) {

                    initRecyclerView(viewHorizontalListView);

                } else {

//                        String result = response.errorBody().string();
                }
            }

            @Override
            public void onFailure(Call<WeatherFiveDaysModel> call, Throwable t) {

                Log.e("ERROR ", ""+t);
                Log.e("ERROR ", ""+t.getMessage());
                Log.e("ERROR ", ""+t.toString());
                Log.e("ERROR ", ""+call.toString());
                Log.e("ERROR ", ""+call);

            }
        });
    }

    private void initNetworkWeatherFiveDays(final View viewHorizontalListView) {

        Endpoints api = RestClient.getApiService();

        Call<WeatherFiveDaysModel> mainWeatherCall = api.getByCityCountryFiveDays(cityName, BuildConfig.API_KEY, temperatureType);

        mainWeatherCall.enqueue(new Callback<WeatherFiveDaysModel>() {
            @Override
            public void onResponse(Call<WeatherFiveDaysModel> call, Response<WeatherFiveDaysModel> response) {

                itemHorizontalWeather = response.body().getList();

                if (response.isSuccessful()) {

                    initRecyclerView(viewHorizontalListView);

                } else {

//                        String result = response.errorBody().string();
                }
            }

            @Override
            public void onFailure(Call<WeatherFiveDaysModel> call, Throwable t) {

                Log.e("ERROR ", ""+t);
                Log.e("ERROR ", ""+t.getMessage());
                Log.e("ERROR ", ""+t.toString());
                Log.e("ERROR ", ""+call.toString());
                Log.e("ERROR ", ""+call);

            }
        });
    }

    private void initRecyclerView(View viewHorizontalListView) {

        rv_horizontalList = (RecyclerView) viewHorizontalListView.findViewById(R.id.rv_list_horizontal);
        rv_horizontalList.setHasFixedSize(true);

        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());

        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        if (itemHorizontalWeather.size() > 0 & rv_horizontalList != null) {

            rv_horizontalList.setAdapter(new HorizontalListAdapter(itemHorizontalWeather, getContext(), temperatureSymbol));

        }

        rv_horizontalList.setLayoutManager(MyLayoutManager);
    }


}
