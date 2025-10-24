package ru.mirea.ostrovskiy.httpurlconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TextView textViewIp, textViewCity, textViewRegion, textViewWeather;
    private Button buttonGetInfo;
    private final String TAG = "HttpConnection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewIp = findViewById(R.id.textViewIp);
        textViewCity = findViewById(R.id.textViewCity);
        textViewRegion = findViewById(R.id.textViewRegion);
        textViewWeather = findViewById(R.id.textViewWeather);
        buttonGetInfo = findViewById(R.id.buttonGetInfo);

        buttonGetInfo.setOnClickListener(v -> onClick());
    }

    private void onClick() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = null;
        if (connectivityManager != null) {
            networkinfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkinfo != null && networkinfo.isConnected()) {
            textViewIp.setText("Загружаем информацию...");
            startDownload();
        } else {
            Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show();
        }
    }

    private void startDownload() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            String ipInfoJson = downloadIpInfo("https://ipinfo.io/json");
            if (ipInfoJson == null) {
                handler.post(() -> Toast.makeText(this, "Ошибка получения IP", Toast.LENGTH_SHORT).show());
                return;
            }

            try {
                JSONObject responseJson = new JSONObject(ipInfoJson);
                String ip = responseJson.getString("ip");
                String city = responseJson.getString("city");
                String region = responseJson.getString("region");
                String loc = responseJson.getString("loc");
                String[] coordinates = loc.split(",");
                String latitude = coordinates[0];
                String longitude = coordinates[1];

                handler.post(() -> {
                    textViewIp.setText("IP-адрес: " + ip);
                    textViewCity.setText("Город: " + city);
                    textViewRegion.setText("Регион: " + region);
                    textViewWeather.setText("Загружаем погоду...");
                });

                String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current_weather=true";
                String weatherJson = downloadIpInfo(weatherUrl);
                if (weatherJson == null) {
                    handler.post(() -> Toast.makeText(this, "Ошибка получения погоды", Toast.LENGTH_SHORT).show());
                    return;
                }

                JSONObject weatherResponse = new JSONObject(weatherJson);
                JSONObject currentWeather = weatherResponse.getJSONObject("current_weather");
                String temperature = currentWeather.getString("temperature");
                String windspeed = currentWeather.getString("windspeed");

                String weatherResult = "Температура: " + temperature + "°C\nСкорость ветра: " + windspeed + " км/ч";

                handler.post(() -> textViewWeather.setText(weatherResult));

            } catch (JSONException e) {
                e.printStackTrace();
                handler.post(() -> Toast.makeText(this, "Ошибка парсинга JSON", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private String downloadIpInfo(String address) {
        InputStream inputStream = null;
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                inputStream = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read = 0;
                while ((read = inputStream.read()) != -1) {
                    bos.write(read);
                }
                bos.close();
                return bos.toString();
            } else {
                Log.e(TAG, "Error: " + connection.getResponseMessage());
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}