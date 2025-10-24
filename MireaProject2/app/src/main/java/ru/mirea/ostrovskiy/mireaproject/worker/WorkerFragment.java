package ru.mirea.ostrovskiy.mireaproject.worker;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ru.mirea.ostrovskiy.mireaproject.databinding.FragmentWorkerBinding;

public class WorkerFragment extends Fragment {

    private FragmentWorkerBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWorkerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonStartWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WorkRequest uploadWorkRequest =
                        new OneTimeWorkRequest.Builder(UploadWorker.class)
                                .build();
                WorkManager
                        .getInstance(getContext())
                        .enqueue(uploadWorkRequest);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}