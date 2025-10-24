package ru.mirea.ostrovskiy.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.mirea.ostrovskiy.camera.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 100;
    private boolean isWork = false;
    private Uri imageUri;
    private ActivityMainBinding binding;
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkPermissions();

        ActivityResultCallback<ActivityResult> callback = result -> {
            if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                Intent data = result.getData();
                binding.imageView.setImageURI(imageUri);
            }
        };
        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                callback);

        binding.button.setOnClickListener(v -> {
            Log.d("CAMERA_APP_DEBUG", "Кнопка 'Сделать фото' нажата.");
            if (isWork) {
                Log.d("CAMERA_APP_DEBUG", "isWork = true. Разрешения есть. Запускаем создание файла и камеры.");
                try {
                    File photoFile = createImageFile();
                    String authorities = getApplicationContext().getPackageName() + ".fileprovider";
                    Log.d("CAMERA_APP_DEBUG", "FileProvider authorities: " + authorities);

                    imageUri = FileProvider.getUriForFile(MainActivity.this, authorities, photoFile);

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    cameraActivityResultLauncher.launch(cameraIntent);
                    Log.d("CAMERA_APP_DEBUG", "Запуск камеры...");
                } catch (Exception e) {
                    Log.e("CAMERA_APP_DEBUG", "КРИТИЧЕСКАЯ ОШИБКА при запуске камеры: ", e);
                    e.printStackTrace();
                }
            } else {
                Log.w("CAMERA_APP_DEBUG", "isWork = false. Разрешений нет. Запрашиваем checkPermissions().");
                checkPermissions();
            }
        });
    }

    private void checkPermissions() {

        int cameraPermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (cameraPermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isWork = true;
                Log.d("CAMERA_APP_DEBUG", "Пользователь дал разрешение на камеру. isWork теперь true.");
            } else {
                isWork = false;
                Log.w("CAMERA_APP_DEBUG", "Пользователь ОТКЛОНИЛ разрешение на камеру. isWork теперь false.");
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDirectory);
    }
}