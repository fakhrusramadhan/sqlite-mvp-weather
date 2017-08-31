package com.fakhrus.weatherbootcamp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fakhrus.weatherbootcamp.WeatherBootCamp;
import com.fakhrus.weatherbootcamp.model.ItemOfWeatherModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Fakhrus on 8/22/17.
 */

public class SharedPref {

    private static SharedPreferences getPref() {
        Context context = WeatherBootCamp.getContext();
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static void saveStringList(String key, List<String> value) {

        Set<String> set = new HashSet<String>();
        set.addAll(value);

        getEditor().putStringSet(key, set).apply();
    }

    public static List<String> getStringList(String key) {



        Set<String> set = getPref().getStringSet(key, null);
        List<String> listString = new ArrayList<String>(set);

        return listString;
    }

    public static void saveObject (String key, List<ItemOfWeatherModel> value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);

        getEditor().putString(key, json).apply();
    }

    public static List<ItemOfWeatherModel> getObject (String key){

        Gson gson = new Gson();
        String json = getPref().getString(key, null);
        Type type = new TypeToken<ArrayList<ItemOfWeatherModel>>() {}.getType();
        ArrayList<ItemOfWeatherModel> arrayList = gson.fromJson(json, type);

        return arrayList;
    }





    public static void saveString(String key, String value) {
        getEditor().putString(key, value).apply();
    }

    public static String getString(String key) {
        return getPref().getString(key, null);
    }

    public static void saveInt(String key, int value) {
        getEditor().putInt(key, value).apply();
    }

    public static int getInt(String key) {
        return getPref().getInt(key, 0);
    }


    public static void saveBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key) {
        return getPref().getBoolean(key, false);
    }

    public static void saveLong(String key, Long value) {
        getEditor().putLong(key, value).apply();
    }

    public static Long getLong(String key) {
        return getPref().getLong(key, 0);
    }

    public static void saveFloat(String key, float value) {
        getEditor().putFloat(key, value).apply();
    }

    public static float getFloat(String key) {
        return getPref().getFloat(key, 0);
    }

    public static void remove(String key) {
        getEditor().remove(key).apply();
    }

    public static boolean check(String key) {
        return getPref().contains(key);
    }

    public static SharedPreferences.Editor getEditor() {
        return getPref().edit();
    }

}
