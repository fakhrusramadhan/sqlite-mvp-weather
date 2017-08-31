package com.fakhrus.weatherbootcamp.network;

import com.fakhrus.weatherbootcamp.model.WeatherFiveDaysModel;
import com.fakhrus.weatherbootcamp.model.WeatherModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Fakhrus on 8/21/17.
 */

public interface Endpoints {


    @GET ("/data/2.5/weather")
    Call<WeatherModel> getByCityCountry (@Query("q") String q,
                                         @Query("APPID") String id,
                                         @Query("units") String unit);

    @GET ("/data/2.5/forecast")
    Call<WeatherFiveDaysModel> getByCityCountryFiveDays (@Query("q") String q,
                                                         @Query("APPID") String id,
                                                         @Query("units") String unit);

    @GET ("/data/2.5/forecast")
    Call<WeatherFiveDaysModel> getByCityCountryFiveDaysByGPS (@Query("lat") String lat,
                                                               @Query("lon") String lon,
                                                               @Query("APPID") String id,
                                                               @Query("units") String unit);

    @GET ("/data/2.5/weather")
    Call<WeatherModel> getByCoordinate (@Query("lat") String lat,
                                      @Query("lon") String lon,
                                      @Query("APPID") String id,
                                      @Query("units") String unit);

}
