package ru.mirea.ostrovskiy.lesson4;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ru.mirea.ostrovskiy.lesson4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.textViewMirea.setText("Это мой TextView");
        binding.editTextMirea.setText("Мой номер по списку №20");

        binding.buttonMirea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredText = binding.editTextMirea.getText().toString();
                binding.textViewMirea.setText(enteredText);
                Log.d("MyButtonClick", "Текст из EditText: " + enteredText);
            }
        });
    }
}