package ru.mirea.ostrovskiy.workmanagerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;
import android.view.View;

import ru.mirea.ostrovskiy.workmanagerapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonStartWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkRequest uploadWorkRequest =
                        new OneTimeWorkRequest.Builder(UploadWorker.class)
                                .build();
                WorkManager
                        .getInstance(getApplicationContext())
                        .enqueue(uploadWorkRequest);
            }
        });
    }
}