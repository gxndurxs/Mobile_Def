package ru.mirea.ostrovskiy.mireaproject.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import ru.mirea.ostrovskiy.mireaproject.databinding.FragmentHardwareBinding;

public class HardwareFragment extends Fragment implements SensorEventListener {

    private FragmentHardwareBinding binding;
    private static final String TAG = "HardwareFragment";

    private SensorManager sensorManager;
    private Sensor orientationSensor;

    private Uri imageUri;
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;

    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private String recordFilePath = null;
    private boolean isStartRecording = true;
    private boolean isStartPlaying = true;

    private ActivityResultLauncher<String[]> permissionsLauncher;
    private boolean arePermissionsGranted = false;

    public HardwareFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHardwareBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initializePermissions();
        initializeSensor();
        initializeCamera();
        initializeAudio();

        checkAndRequestPermissions();

        return root;
    }

    private void initializePermissions() {
        permissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), (Map<String, Boolean> permissions) -> {
            arePermissionsGranted = Boolean.TRUE.equals(permissions.get(Manifest.permission.CAMERA))
                    && Boolean.TRUE.equals(permissions.get(Manifest.permission.RECORD_AUDIO));

            if (!arePermissionsGranted) {
                binding.buttonTakePhoto.setEnabled(false);
                binding.buttonRecord.setEnabled(false);
                binding.buttonPlay.setEnabled(false);
            }
        });
    }

    private void checkAndRequestPermissions() {
        boolean isCameraGranted = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean isAudioGranted = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;

        if (isCameraGranted && isAudioGranted) {
            arePermissionsGranted = true;
        } else {
            permissionsLauncher.launch(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO});
        }
    }

    private void initializeSensor() {
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float azimuth = event.values[0];
            binding.textViewAzimuth.setText(String.format(Locale.getDefault(), "Азимут: %.2f", azimuth));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void initializeCamera() {
        cameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == requireActivity().RESULT_OK) {
                binding.imageViewProfile.setImageURI(imageUri);
            }
        });

        binding.buttonTakePhoto.setOnClickListener(v -> {
            if (arePermissionsGranted) {
                try {
                    File photoFile = createImageFile();
                    String authorities = requireActivity().getApplicationContext().getPackageName() + ".fileprovider";
                    imageUri = FileProvider.getUriForFile(requireContext(), authorities, photoFile);

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    cameraActivityResultLauncher.launch(cameraIntent);
                } catch (IOException e) {
                    Log.e(TAG, "Error creating image file", e);
                }
            } else {
                checkAndRequestPermissions();
            }
        });
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private void initializeAudio() {
        File recordFile = new File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC), "mireaproject_audiorecord.3gp");
        recordFilePath = recordFile.getAbsolutePath();

        binding.buttonPlay.setEnabled(false);

        binding.buttonRecord.setOnClickListener(v -> {
            if (arePermissionsGranted) {
                if (isStartRecording) {
                    startRecording();
                    binding.buttonRecord.setText("Остановить запись");
                    binding.buttonPlay.setEnabled(false);
                } else {
                    stopRecording();
                    binding.buttonRecord.setText("Начать запись");
                    binding.buttonPlay.setEnabled(true);
                }
                isStartRecording = !isStartRecording;
            } else {
                checkAndRequestPermissions();
            }
        });

        binding.buttonPlay.setOnClickListener(v -> {
            if (isStartPlaying) {
                startPlaying();
                binding.buttonPlay.setText("Остановить");
                binding.buttonRecord.setEnabled(false);
            } else {
                stopPlaying();
                binding.buttonPlay.setText("Воспроизвести");
                binding.buttonRecord.setEnabled(true);
            }
            isStartPlaying = !isStartPlaying;
        });
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            Log.e(TAG, "startRecording prepare() failed", e);
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(recordFilePath);
            player.setOnCompletionListener(mp -> {
                stopPlaying();
                binding.buttonPlay.setText("Воспроизвести");
                binding.buttonRecord.setEnabled(true);
                isStartPlaying = true;
            });
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(TAG, "startPlaying prepare() failed", e);
        }
    }

    private void stopPlaying() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (player != null) {
            player.release();
            player = null;
        }
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
        binding = null;
    }
}