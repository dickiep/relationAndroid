package com.dickies.android.relationbn.productdisplay;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Instantiates the Retrofit library to work with the database
 */
public class ApiClient {

    /**
     * Constant that provides the URL for the php which operates on the database
     */
    public static final String BASE_URL = "http://pdickie01.web.eeecs.qub.ac.uk/relation/v1/";

    /**
     * Retrofit library constant
     */
    public static Retrofit retrofit;

    /**
     * Returns the Retrofit library Api Client
     * @return
     */
    public static Retrofit getApiClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}
