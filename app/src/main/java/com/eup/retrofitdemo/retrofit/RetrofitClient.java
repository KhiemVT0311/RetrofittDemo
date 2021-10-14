package com.eup.retrofitdemo.retrofit;

import android.text.format.Time;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl){
        OkHttpClient builder = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS) //data reading time
                .writeTimeout(5,TimeUnit.SECONDS) //data reading time
                .connectTimeout(10,TimeUnit.SECONDS) //connection time
                .retryOnConnectionFailure(true)
                .build();//auto reconnect
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))//chuyển dữ liệu gson về java
                .build();

        return retrofit;
    }
}
