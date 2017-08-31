package com.fakhrus.weatherbootcamp.feature.main;

import com.fakhrus.weatherbootcamp.model.WeatherModel;

/**
 * Created by Fakhrus on 8/21/17.
 */

public interface MainView {

    void showData(WeatherModel mainWeather);
    void showError();
    void showToast(String data);
    void initBlurBackground();
    void showLoadingContent();
    void dismissLoadingContent();
    void showNoConnectionDialog();
//    void dismissNoConnectionDialog();
}
