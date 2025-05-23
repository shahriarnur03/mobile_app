package com.example.weatherbasic;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    // References to UI elements in the XML
    private TextView cityNameText, temperatureText, humidityText, descriptionText, windText, pressureText;
    private ImageView weatherIcon;
    private EditText cityNameInput;
    private ImageView searchIcon;

    // Replace with your OpenWeatherMap API Key
    private static final String API_KEY = "62bd3a0e9a285ffa0ca9cb7c23cf67b7";  // Example API Key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind the UI components from XML to Java variables
        cityNameText = findViewById(R.id.locationTV);          // Location TextView (City Name)
        temperatureText = findViewById(R.id.tempTV);          // Temperature TextView
        humidityText = findViewById(R.id.humidityTV);        // Humidity TextView
        windText = findViewById(R.id.windSpeedTV);           // Wind Speed TextView
        descriptionText = findViewById(R.id.weatherTypeTV);  // Weather description TextView
        weatherIcon = findViewById(R.id.weatherTypeIV);      // Weather Icon ImageView
        pressureText = findViewById(R.id.pressureTV);        // Pressure TextView
        cityNameInput = findViewById(R.id.etSearch);          // EditText for city name input
        searchIcon = findViewById(R.id.ivSearchIcon);         // ImageView for the search icon

        // Fetch weather data for the default city "Dhaka" on startup
        FetchWeatherData("Dhaka");

        // Set onClickListener for the search icon to fetch weather for the entered city
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = cityNameInput.getText().toString().trim();
                if (!cityName.isEmpty()) {
                    FetchWeatherData(cityName); // Fetch the weather for the entered city
                } else {
                    cityNameInput.setError("Please enter a city name");
                }
            }
        });
    }

    private void FetchWeatherData(String cityName) {
        // Construct the URL to fetch weather data for the given city
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + API_KEY + "&units=metric";

        // Run the network request on a background thread
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                String result = response.body().string();  // Get the response as a string
                runOnUiThread(() -> updateUI(result));  // Update the UI on the main thread
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateUI(String result) {
        // Check if the result is valid and then parse it
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject main = jsonObject.getJSONObject("main");
                double temperature = main.getDouble("temp");
                double humidity = main.getDouble("humidity");
                double windSpeed = jsonObject.getJSONObject("wind").getDouble("speed");
                double pressure = main.getDouble("pressure");

                String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                String iconCode = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");

                // Dynamically set the weather icon based on the weather condition
                String resourceName = "";

                // Determine which icon to use based on the weather description
                if (description.contains("clear") && iconCode.endsWith("d")) {
                    resourceName = "ic_clear_sky_day";  // Clear weather during the day
                } else if (description.contains("clear") && iconCode.endsWith("n")) {
                    resourceName = "ic_clear_sky_night";  // Clear weather at night
                } else if (description.contains("cloud")) {
                    resourceName = "ic_cloudy_weather";  // Cloudy weather
                } else if (description.contains("rain")) {
                    resourceName = "ic_rainy_weather";  // Rainy weather
                } else if (description.contains("thunderstorm")) {
                    resourceName = "ic_thunderstorm_weather";  // Thunderstorm
                } else if (description.contains("snow")) {
                    resourceName = "ic_snowy_weather";  // Snowy weather
                }

                // Dynamically set the weather icon
                int resId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
                if (resId != 0) {
                    weatherIcon.setImageResource(resId);  // Set the appropriate weather icon
                } else {
                    weatherIcon.setImageResource(R.drawable.ic_cloudy_weather); // Default fallback icon if not found
                }

                // Update the TextViews with weather information
                cityNameText.setText(jsonObject.getString("name"));
                temperatureText.setText(String.format("%.2f°", temperature));  // Display temperature as double
                humidityText.setText(String.format("%.2f%%", humidity));  // Display humidity as double
                windText.setText(String.format("%.2f km/h", windSpeed));  // Display wind speed as double
                descriptionText.setText(description);  // Weather description
                pressureText.setText(String.format("%.2f hPa", pressure));  // Display pressure as double

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
