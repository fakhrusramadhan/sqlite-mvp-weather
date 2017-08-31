package com.fakhrus.weatherbootcamp.feature.main;

/**
 * Created by Fakhrus on 8/21/17.
 */

public interface MainPresenter {

//    void initMainWeatherNetwork(String city, String country, String API_KEY);
    void initMainWeatherNetwork(String city, String temperatureType, String API_KEY);
    void initMainGPSWeatherNetwork(String latitude, String longitude, String temperatureType, String API_KEY);

}
