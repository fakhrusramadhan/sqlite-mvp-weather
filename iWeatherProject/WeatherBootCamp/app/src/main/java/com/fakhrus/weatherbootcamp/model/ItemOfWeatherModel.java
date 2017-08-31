package com.fakhrus.weatherbootcamp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Fakhrus on 8/22/17.
 */

public class ItemOfWeatherModel {

    public String cityName;
    public String temperature;
    public String weatherType;
    public String weatherDescription;

//    public ItemOfWeatherModel(String cityName, int temperature, String weatherType, String weatherDescription) {
//        this.cityName = cityName;
//        this.temperature = temperature;
//        this.weatherType = weatherType;
//        this.weatherDescription = weatherDescription;
//    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }
}
