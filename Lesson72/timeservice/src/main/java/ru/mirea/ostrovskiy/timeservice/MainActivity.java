package ru.mirea.ostrovskiy.timeservice;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "TimeService";
    private final String host = "time.nist.gov";
    private final int port = 13;
    private TextView textViewTime;
    private Button buttonGetTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTime = findViewById(R.id.textViewTime);
        buttonGetTime = findViewById(R.id.buttonGetTime);

        buttonGetTime.setOnClickListener(v -> getTimeFromServer());
    }

    private void getTimeFromServer() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                Socket socket = new Socket(host, port);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                reader.readLine();
                String timeResult = reader.readLine();
                socket.close();

                String[] parts = timeResult.split(" ");
                String dateTimeUtcStr = parts[1] + " " + parts[2];

                SimpleDateFormat utcFormatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.getDefault());
                utcFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));

                Date dateInUtc = utcFormatter.parse(dateTimeUtcStr);

                SimpleDateFormat localFormatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.getDefault());
                localFormatter.setTimeZone(TimeZone.getDefault());

                String localTimeStr = localFormatter.format(dateInUtc);
                String[] localParts = localTimeStr.split(" ");
                String finalResult = "Дата: " + localParts[0] + "\nВремя: " + localParts[1];


                handler.post(() -> {
                    Log.d(TAG, "UTC Time: " + timeResult);
                    Log.d(TAG, "Local Time: " + finalResult);
                    textViewTime.setText(finalResult);
                });

            } catch (IOException | ParseException e) {
                e.printStackTrace();
                handler.post(() -> textViewTime.setText("Ошибка: не удалось получить или преобразовать время"));
            }
        });
    }
}