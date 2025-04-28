package com.example.basicweather;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basicweather.models.WeatherResponse;
import com.example.basicweather.network.WeatherService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView weatherTypeTV, locationTV, tempTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        weatherTypeTV = findViewById(R.id.weatherTypeTV);
        locationTV = findViewById(R.id.locationTV);
        tempTV = findViewById(R.id.tempTV);

        // Fetch weather data
        fetchWeatherData("Dhaka");
    }

    private void fetchWeatherData(String cityName) {
        WeatherService.getInstance().getWeather(cityName, "62bd3a0e9a285ffa0ca9cb7c23cf67b7\n" + //
                        "")
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            WeatherResponse weather = response.body();
                            updateUI(weather);
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to fetch weather data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(WeatherResponse weather) {
        weatherTypeTV.setText(weather.getWeather().get(0).getDescription());
        locationTV.setText(weather.getName());
        tempTV.setText(String.format("%s°", weather.getMain().getTemp()));
    }
}