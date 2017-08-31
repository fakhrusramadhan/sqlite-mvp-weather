package com.fakhrus.weatherbootcamp.feature.intro;

/**
 * Created by Fakhrus on 8/23/17.
 */

public interface IntroView {

    String getCityName();
    void showLoadingDialog();
    void dismissLoadingDialog();
//    void showNoConnectionDialog();
//    void dismissNoConnectionDialog();
}
