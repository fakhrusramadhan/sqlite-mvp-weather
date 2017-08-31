package com.fakhrus.weatherbootcamp.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Fakhrus on 8/21/17.
 */

public class RestClient {

    private static final String ROOT_URL = "https://api.openweathermap.org";

    private static Retrofit getRetrofitInstance() {

        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        okhttpClientBuilder.addInterceptor(logging);

        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClientBuilder.build())
                .build();
    }

    public static Endpoints getApiService() {
        return getRetrofitInstance().create(Endpoints.class);
    }
}
