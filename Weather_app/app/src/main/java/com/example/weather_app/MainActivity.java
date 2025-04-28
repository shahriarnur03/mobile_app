package com.example.weather_app;

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

    private TextView cityNameText, temperatureText, humidityText, descriptionText, windText, pressureText;
    private ImageView weatherIcon;
    private EditText cityNameInput;
    private ImageView searchIcon;


    private static final String API_KEY = "62bd3a0e9a285ffa0ca9cb7c23cf67b7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cityNameText = findViewById(R.id.locationTV);
        temperatureText = findViewById(R.id.tempTV);
        humidityText = findViewById(R.id.humidityTV);
        windText = findViewById(R.id.windSpeedTV);
        descriptionText = findViewById(R.id.weatherTypeTV);
        weatherIcon = findViewById(R.id.weatherTypeIV);
        pressureText = findViewById(R.id.pressureTV);
        cityNameInput = findViewById(R.id.etSearch);
        searchIcon = findViewById(R.id.ivSearchIcon);

        FetchWeatherData("Dhaka");


        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = cityNameInput.getText().toString().trim();
                if (!cityName.isEmpty()) {
                    FetchWeatherData(cityName);
                } else {
                    cityNameInput.setError("Please enter a city name");
                }
            }
        });
    }

    private void FetchWeatherData(String cityName) {

        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + API_KEY + "&units=metric";


        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                runOnUiThread(() -> updateUI(result));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateUI(String result) {

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

                String resourceName = "";


                if (description.contains("clear") && iconCode.endsWith("d")) {
                    resourceName = "ic_clear_sky_day";
                } else if (description.contains("clear") && iconCode.endsWith("n")) {
                    resourceName = "ic_clear_sky_night";
                } else if (description.contains("cloud")) {
                    resourceName = "ic_cloudy_weather";
                } else if (description.contains("rain")) {
                    resourceName = "ic_rainy_weather";
                } else if (description.contains("thunderstorm")) {
                    resourceName = "ic_thunderstorm_weather";
                } else if (description.contains("snow")) {
                    resourceName = "ic_snowy_weather";
                }

                // Dynamically set the weather icon
                int resId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
                if (resId != 0) {
                    weatherIcon.setImageResource(resId);
                } else {
                    weatherIcon.setImageResource(R.drawable.ic_clear_sky_day);
                }

                // Update the TextViews with weather information
                cityNameText.setText(jsonObject.getString("name"));
                temperatureText.setText(String.format("%.2f°", temperature));
                humidityText.setText(String.format("%.2f%%", humidity));
                windText.setText(String.format("%.2f km/h", windSpeed));
                descriptionText.setText(description);
                pressureText.setText(String.format("%.2f hPa", pressure));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color));
    }

}
