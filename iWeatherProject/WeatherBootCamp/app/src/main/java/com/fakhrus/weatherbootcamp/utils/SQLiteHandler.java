package com.fakhrus.weatherbootcamp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fakhrus.weatherbootcamp.model.ItemOfWeatherModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fakhrus on 8/23/17.
 */

public class SQLiteHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_NAME = "item_weather";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_CITY = "city_name";
    private static final String KEY_TEMPERATURE = "temperature";
    private static final String KEY_TYPE = "type";
    private static final String KEY_DESCRIPTION = "description";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WEATHER_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CITY + " TEXT,"
                + KEY_TEMPERATURE + " INT," + KEY_TYPE + " TEXT," + KEY_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_WEATHER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new weather
    public void addWeather(ItemOfWeatherModel itemWeather) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CITY, itemWeather.getCityName()); // City Name
        values.put(KEY_TEMPERATURE, itemWeather.getTemperature());
        values.put(KEY_TYPE, itemWeather.getWeatherType());
        values.put(KEY_DESCRIPTION, itemWeather.getWeatherDescription());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }



    // Getting All Weather
    public List<ItemOfWeatherModel> getAllItemWeather() {
        List<ItemOfWeatherModel> weatherList = new ArrayList<ItemOfWeatherModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ItemOfWeatherModel itemOfWeatherModel = new ItemOfWeatherModel();
                itemOfWeatherModel.setCityName(cursor.getString(1));
                itemOfWeatherModel.setTemperature(cursor.getString(2));
                itemOfWeatherModel.setWeatherType(cursor.getString(3));
                itemOfWeatherModel.setWeatherDescription(cursor.getString(4));
                // Adding itemWeather to list
                weatherList.add(itemOfWeatherModel);
            } while (cursor.moveToNext());
        }

        // return weather list
        return weatherList;
    }


    // Deleting single weather
    public void deleteWeather(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }


}
