package ru.mirea.ostrovskiy.mireaproject.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
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

import ru.mirea.ostrovskiy.mireaproject.databinding.FragmentPhotoGalleryBinding;

public class PhotoGalleryFragment extends Fragment {

    private FragmentPhotoGalleryBinding binding;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private Uri currentPhotoUri;
    private PhotoAdapter adapter;
    private List<File> photoFiles;
    private File photoStorageDir;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPhotoGalleryBinding.inflate(inflater, container, false);

        photoStorageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        RecyclerView recyclerView = binding.recyclerViewPhotos;
        photoFiles = new ArrayList<>();
        adapter = new PhotoAdapter(photoFiles);
        recyclerView.setAdapter(adapter);

        loadPhotos();

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == requireActivity().RESULT_OK) {
                loadPhotos();
            }
        });

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                launchCamera();
            }
        });

        binding.fabAddPhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                launchCamera();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        return binding.getRoot();
    }

    private void launchCamera() {
        try {
            File photoFile = createImageFile();
            String authorities = requireContext().getApplicationContext().getPackageName() + ".fileprovider";
            currentPhotoUri = FileProvider.getUriForFile(requireContext(), authorities, photoFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);
            cameraLauncher.launch(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        return File.createTempFile(imageFileName, ".jpg", photoStorageDir);
    }

    private void loadPhotos() {
        photoFiles.clear();
        File[] files = photoStorageDir.listFiles();
        if (files != null) {
            Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
            photoFiles.addAll(Arrays.asList(files));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}