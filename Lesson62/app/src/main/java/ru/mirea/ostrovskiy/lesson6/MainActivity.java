package ru.mirea.ostrovskiy.lesson6;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText editTextGroup;
    private EditText editTextListNumber;
    private EditText editTextFavoriteMovie;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextGroup = findViewById(R.id.editTextGroup);
        editTextListNumber = findViewById(R.id.editTextListNumber);
        editTextFavoriteMovie = findViewById(R.id.editTextFavoriteMovie);

        sharedPref = getSharedPreferences("mirea_settings", Context.MODE_PRIVATE);

        loadData();
    }

    private void loadData() {
        String group = sharedPref.getString("GROUP", "БСБО-09-22");
        int listNumber = sharedPref.getInt("LIST_NUMBER", 20);
        String movie = sharedPref.getString("FAVORITE_MOVIE", "");

        editTextGroup.setText(group);
        editTextListNumber.setText(String.valueOf(listNumber));
        editTextFavoriteMovie.setText(movie);
    }

    public void onSaveButtonClick(View view) {
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("GROUP", editTextGroup.getText().toString());
        try {
            editor.putInt("LIST_NUMBER", Integer.parseInt(editTextListNumber.getText().toString()));
        } catch (NumberFormatException e) {
            editor.putInt("LIST_NUMBER", 0);
        }
        editor.putString("FAVORITE_MOVIE", editTextFavoriteMovie.getText().toString());

        editor.apply();
    }
}