package com.example.weatherbasic;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText etSearch;
    private TextView weatherTypeTV, locationTV, tempTV, humidityTV, windSpeedTV, pressureTV;

    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static final String API_KEY = "62bd3a0e9a285ffa0ca9cb7c23cf67b7"; // Your OpenWeather API Key

    private WeatherApi weatherApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        etSearch = findViewById(R.id.etSearch);
        weatherTypeTV = findViewById(R.id.weatherTypeTV);
        locationTV = findViewById(R.id.locationTV);
        tempTV = findViewById(R.id.tempTV);
        humidityTV = findViewById(R.id.humidityTV);
        windSpeedTV = findViewById(R.id.windSpeedTV); // Added windSpeedTV TextView
        pressureTV = findViewById(R.id.pressureTV); // Added pressureTV TextView

        ImageView searchButton = findViewById(R.id.ivSearchIcon); // Add the button in your XML layout

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the WeatherApi interface
        weatherApi = retrofit.create(WeatherApi.class);

        // Set an OnClickListener on the search button
        searchButton.setOnClickListener(v -> {
            String cityName = etSearch.getText().toString().trim();
            if (!cityName.isEmpty()) {
                getWeatherData(cityName);  // Call the API when the button is clicked
            } else {
                Toast.makeText(MainActivity.this, "Please enter a city name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getWeatherData(String cityName) {
        // Make the API call
        Call<WeatherResponse> call = weatherApi.getWeather(cityName, API_KEY, "metric");

        // Asynchronous request
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();

                    // Extract data from the response
//                    String description = weatherResponse.getWeather().get(0).getDescription();
                    double temperature = weatherResponse.getMain().getTemp();
                    double humidity = weatherResponse.getMain().getHumidity();
                    double windSpeed = weatherResponse.getWind().getSpeed();
                    double pressure = weatherResponse.getMain().getPressure();

                    String cityName = weatherResponse.getName();

                    // Update the UI with the fetched data
                    updateUI( temperature, humidity, windSpeed, pressure, cityName);
                } else {
                    // Handle error response
                    Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // Handle failure (network issues, etc.)
                Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI( double temperature, double humidity, double windSpeed, double pressure, String cityName) {
        // Set the fetched data into your TextViews
//        weatherTypeTV.setText(description);
        locationTV.setText(cityName);
        tempTV.setText(String.format("%.2f°", temperature));
        humidityTV.setText(String.format("%.0f%%", humidity));
        windSpeedTV.setText(String.format("%.2f km/h", windSpeed)); // Display wind speed
        pressureTV.setText(String.format("%.0f hPa", pressure)); // Display pressure
    }
}
