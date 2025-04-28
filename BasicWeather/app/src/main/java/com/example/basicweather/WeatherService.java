package com.example.basicweather.network;

import com.example.basicweather.models.WeatherResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("weather")
    Call<WeatherResponse> getWeather(@Query("q") String cityName, @Query("appid") String apiKey);

    static WeatherService getInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/") // Base URL for OpenWeather API
                .addConverterFactory(GsonConverterFactory.create()) // JSON to Java object conversion
                .build();
        return retrofit.create(WeatherService.class);
    }
}
