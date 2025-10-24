package ru.mirea.ostrovskiy.favoritebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ShareActivity extends AppCompatActivity {

    private EditText editTextBookName;
    private EditText editTextQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        editTextBookName = findViewById(R.id.editTextBookName);
        editTextQuote = findViewById(R.id.editTextQuote);
        Button buttonSend = findViewById(R.id.button_send_data);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();

                String bookName = editTextBookName.getText().toString();
                String quote = editTextQuote.getText().toString();

                String resultString = "Название Вашей любимой книги: " + bookName + "\nЦитата: " + quote;

                data.putExtra(MainActivity.USER_MESSAGE_KEY, resultString);

                setResult(Activity.RESULT_OK, data);

                finish();
            }
        });
    }
}