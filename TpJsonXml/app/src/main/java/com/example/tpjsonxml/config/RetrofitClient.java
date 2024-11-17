package com.example.tpjsonxml.config;

import com.example.tpjsonxml.api.ApiService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jaxb.JaxbConverterFactory;


public class RetrofitClient {

    private static final String BASE_URL = "http://10.0.0.1:8082/";

    static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(JaxbConverterFactory.create())
            .build();

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static ApiService getApi() {
        return retrofit.create(ApiService.class);
    }
}
