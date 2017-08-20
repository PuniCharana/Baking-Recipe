package com.example.android.bakingrecipe.network;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    private static Retrofit retrofit = null;

    private static final OkHttpClient httpClient = new OkHttpClient();

    private static final GsonConverterFactory gsonFactory = GsonConverterFactory.create();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(gsonFactory)
                    .client(httpClient)
                    .build();
        }
        return retrofit;
    }
}