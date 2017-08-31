package com.fakhrus.weatherbootcamp.feature.city_list;

import com.fakhrus.weatherbootcamp.model.WeatherModel;

/**
 * Created by Fakhrus on 8/22/17.
 */

public interface SelectCityView {

    void showData(WeatherModel addWeather);
    void showError();
    void showToast(String data);
    void showDataCurentLocation(WeatherModel currentLocation);
    void loadingAddWeatherOn();
    void loadingAddWeatherOff();
    void loadingCurrentLocationOn();
    void loadingCurrentLocationOff();
    void showloadingDialog();
    void dismissloadingDialog();
    void showNoConnectionDialog();
//    void dismissNoConnectionDialog();
}
