package com.fakhrus.weatherbootcamp.feature.city_list;

/**
 * Created by Fakhrus on 8/22/17.
 */

public interface SelectCityPresenter {

    void networkAddCity(String city, String units);
    void networkCurrentLocation(String latutude, String lingutude, String units);

}
