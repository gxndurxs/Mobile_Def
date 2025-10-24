package ru.mirea.tenyutin.buttonclicker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvOut;
    private Button btnWhoAmI;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOut = findViewById(R.id.tvOut);
        btnWhoAmI = findViewById(R.id.btnWhoAmI);
        checkBox = findViewById(R.id.checkBoxAgree);

        btnWhoAmI.setOnClickListener(v -> {
            tvOut.setText("Мой номер по списку № 24");
            checkBox.setChecked(true);
        });
    }

    @SuppressWarnings("unused")
    public void onItIsNotMeClick(View view) {
        tvOut.setText("Это не я сделал");
        checkBox.setChecked(false);
    }
}

