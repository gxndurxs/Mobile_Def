package ru.mirea.ostrovskiy.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FirebaseAuth";

    private static final String PREFS_FILE = "auth_settings";
    private static final String KEY_EMAIL = "saved_email";

    private TextView textViewStatus, textViewDetail;
    private EditText editTextEmail, editTextPassword;
    private Button buttonSignIn, buttonCreateAccount, buttonSignOut, buttonVerifyEmail;
    private LinearLayout emailPasswordFields, emailPasswordButtons, signedInButtons;
    private CheckBox checkBoxRememberMe;
    private SharedPreferences sharedPreferences;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        textViewStatus = findViewById(R.id.textViewStatus);
        textViewDetail = findViewById(R.id.textViewDetail);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        buttonSignOut = findViewById(R.id.buttonSignOut);
        buttonVerifyEmail = findViewById(R.id.buttonVerifyEmail);
        emailPasswordFields = findViewById(R.id.emailPasswordFields);
        emailPasswordButtons = findViewById(R.id.emailPasswordButtons);
        signedInButtons = findViewById(R.id.signedInButtons);
        checkBoxRememberMe = findViewById(R.id.checkBoxRememberMe);

        buttonSignIn.setOnClickListener(this);
        buttonCreateAccount.setOnClickListener(this);
        buttonSignOut.setOnClickListener(this);
        buttonVerifyEmail.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        loadSavedEmail();
    }

    private void loadSavedEmail() {
        String savedEmail = sharedPreferences.getString(KEY_EMAIL, null);
        if (savedEmail != null) {
            editTextEmail.setText(savedEmail);
            checkBoxRememberMe.setChecked(true);
        }
    }

    private void saveOrClearEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (checkBoxRememberMe.isChecked()) {
            editor.putString(KEY_EMAIL, email);
        } else {
            editor.remove(KEY_EMAIL);
        }
        editor.apply();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            textViewStatus.setText("Signed In");
            textViewDetail.setText("Email: " + user.getEmail() + "\nUID: " + user.getUid() + "\nVerified: " + user.isEmailVerified());
            emailPasswordFields.setVisibility(View.GONE);
            emailPasswordButtons.setVisibility(View.GONE);
            signedInButtons.setVisibility(View.VISIBLE);
            buttonVerifyEmail.setEnabled(!user.isEmailVerified());
        } else {
            textViewStatus.setText("Signed Out");
            textViewDetail.setText(null);
            emailPasswordFields.setVisibility(View.VISIBLE);
            emailPasswordButtons.setVisibility(View.VISIBLE);
            signedInButtons.setVisibility(View.GONE);
        }
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        saveOrClearEmail(email);
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        saveOrClearEmail(email);
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        buttonVerifyEmail.setEnabled(false);
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;
        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    buttonVerifyEmail.setEnabled(true);
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "sendEmailVerification", task.getException());
                        Toast.makeText(MainActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (i == R.id.buttonCreateAccount) {
            createAccount(email, password);
        } else if (i == R.id.buttonSignIn) {
            signIn(email, password);
        } else if (i == R.id.buttonSignOut) {
            signOut();
        } else if (i == R.id.buttonVerifyEmail) {
            sendEmailVerification();
        }
    }
}