package ru.mirea.ostrovskiy.mireaproject.filework;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import ru.mirea.ostrovskiy.mireaproject.MainActivity;
import ru.mirea.ostrovskiy.mireaproject.R;

public class FileWorkerFragment extends Fragment {

    private TextView textViewFileContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_file_worker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewFileContent = view.findViewById(R.id.textViewFileContent);

        FloatingActionButton fab = ((MainActivity) requireActivity()).findViewById(R.id.fab);
        fab.setOnClickListener(v -> showAddFileDialog());
    }

    private void showAddFileDialog() {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_file, null);
        final EditText editTextFileName = dialogView.findViewById(R.id.editTextDialogFileName);
        final EditText editTextFileContent = dialogView.findViewById(R.id.editTextDialogFileContent);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Создать/Загрузить файл");
        dialogBuilder.setMessage("Введите имя файла и содержимое для шифрования");

        dialogBuilder.setPositiveButton("Сохранить", (dialog, which) -> {
            String fileName = editTextFileName.getText().toString();
            String content = editTextFileContent.getText().toString();
            if (!fileName.isEmpty() && !content.isEmpty()) {
                saveFileEncrypted(fileName, content);
            } else {
                Toast.makeText(getContext(), "Имя файла и содержимое не могут быть пустыми", Toast.LENGTH_SHORT).show();
            }
        });

        dialogBuilder.setNegativeButton("Загрузить", (dialog, which) -> {
            String fileName = editTextFileName.getText().toString();
            if (!fileName.isEmpty()) {
                loadFileDecrypted(fileName);
            } else {
                Toast.makeText(getContext(), "Имя файла не может быть пустым", Toast.LENGTH_SHORT).show();
            }
        });

        dialogBuilder.setNeutralButton("Отмена", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void saveFileEncrypted(String fileName, String content) {
        try {
            FileOutputStream fos = requireActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
            String encryptedContent = Base64.encodeToString(content.getBytes(), Base64.DEFAULT);
            fos.write(encryptedContent.getBytes());
            fos.close();
            Toast.makeText(getContext(), "Файл зашифрован и сохранен!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка сохранения", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFileDecrypted(String fileName) {
        try {
            FileInputStream fin = requireActivity().openFileInput(fileName);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            fin.close();
            String encryptedContent = new String(bytes);
            byte[] decodedBytes = Base64.decode(encryptedContent, Base64.DEFAULT);
            String decryptedContent = new String(decodedBytes, StandardCharsets.UTF_8);
            textViewFileContent.setText(decryptedContent);
            Toast.makeText(getContext(), "Файл загружен и расшифрован!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            textViewFileContent.setText("Ошибка: Файл не найден или поврежден");
            Toast.makeText(getContext(), "Ошибка загрузки", Toast.LENGTH_SHORT).show();
        }
    }
}