package ru.mirea.ostrovskiy.looper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ru.mirea.ostrovskiy.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MyLooper myLooper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyLooperApp", "MainActivity onCreate: СТАРТ АКТИВНОСТИ");
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.getData().getString("result");
                Log.d(MainActivity.class.getSimpleName(), "Task execute. This is result: " + result);
            }
        };

        myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        binding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ageStr = binding.editTextAge.getText().toString();
                String jobStr = binding.editTextJob.getText().toString();

                if (ageStr.isEmpty() || jobStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (myLooper == null || myLooper.mHandler == null) {
                    Toast.makeText(MainActivity.this, "Looper еще не готов", Toast.LENGTH_SHORT).show();
                    return;
                }

                Message msg = Message.obtain();
                Bundle bundle = new Bundle();

                try {
                    int age = Integer.parseInt(ageStr);

                    bundle.putString("KEY", "Это сообщение из главного потока");
                    bundle.putString("JOB", jobStr);
                    bundle.putInt("AGE", age);
                    msg.setData(bundle);

                    myLooper.mHandler.sendMessage(msg);

                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Некорректный формат возраста!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}