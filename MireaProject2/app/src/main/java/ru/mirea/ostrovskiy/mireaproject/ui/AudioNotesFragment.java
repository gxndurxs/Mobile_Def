package ru.mirea.ostrovskiy.mireaproject.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.mirea.ostrovskiy.mireaproject.databinding.FragmentAudioNotesBinding;

public class AudioNotesFragment extends Fragment {

    private FragmentAudioNotesBinding binding;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private File audioStorageDir;
    private AudioAdapter adapter;
    private List<File> audioFiles;
    private boolean isRecording = false;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAudioNotesBinding.inflate(inflater, container, false);

        audioStorageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC);

        RecyclerView recyclerView = binding.recyclerViewAudio;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        audioFiles = new ArrayList<>();
        adapter = new AudioAdapter(audioFiles, file -> playAudio(file));
        recyclerView.setAdapter(adapter);

        loadAudioFiles();

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                toggleRecording();
            }
        });

        binding.recordButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                toggleRecording();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
            }
        });

        return binding.getRoot();
    }

    private void toggleRecording() {
        if (isRecording) {
            stopRecording();
        } else {
            startRecording();
        }
    }

    private void startRecording() {
        try {
            binding.recordButton.setText("Остановить запись");
            isRecording = true;
            File audioFile = createAudioFile();
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            Log.e("AudioNotesFragment", "startRecording failed", e);
        }
    }

    private void stopRecording() {
        binding.recordButton.setText("Начать запись");
        isRecording = false;
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            loadAudioFiles();
        }
    }

    private void playAudio(File file) {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e("AudioNotesFragment", "playAudio failed", e);
        }
    }

    private File createAudioFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String audioFileName = "AUDIO_" + timeStamp + "_";
        return File.createTempFile(audioFileName, ".3gp", audioStorageDir);
    }

    private void loadAudioFiles() {
        audioFiles.clear();
        File[] files = audioStorageDir.listFiles((dir, name) -> name.endsWith(".3gp"));
        if (files != null) {
            Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
            audioFiles.addAll(Arrays.asList(files));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}