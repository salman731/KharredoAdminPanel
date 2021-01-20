package com.muqit.KharredoAdminPanel.API;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final String BASE_URL = "https://kharredo.com/";
    private Retrofit retrofit = null;

    public Retrofit getRetrofitClient() {
        Retrofit build = new Builder().baseUrl("https://kharredo.com/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build();
        this.retrofit = build;
        return build;
    }
}
