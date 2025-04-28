package com.example.weatherbasic;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeather(
            @Query("q") String cityName,       // City name (e.g., "Dhaka")
            @Query("appid") String apiKey,     // Your API key
            @Query("units") String units       // Unit type (metric for Celsius)
    );
}
