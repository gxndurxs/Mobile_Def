package ru.mirea.ostrovskiy.mireaproject.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import ru.mirea.ostrovskiy.mireaproject.R;

public class ProfileFragment extends Fragment {

    private TextInputEditText editTextName;
    private TextInputEditText editTextBirthDate;
    private TextInputEditText editTextFavoriteMovie;
    private TextInputEditText editTextFavoriteAnimal;
    private TextInputEditText editTextLocation;
    private Button buttonSaveProfile;
    private SharedPreferences sharedPreferences;

    private static final String PREFS_FILE = "profile_settings";
    private static final String KEY_NAME = "USER_NAME";
    private static final String KEY_BIRTH_DATE = "USER_BIRTH_DATE";
    private static final String KEY_MOVIE = "FAVORITE_MOVIE";
    private static final String KEY_ANIMAL = "FAVORITE_ANIMAL";
    private static final String KEY_LOCATION = "USER_LOCATION";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireActivity().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        editTextName = view.findViewById(R.id.editTextName);
        editTextBirthDate = view.findViewById(R.id.editTextBirthDate);
        editTextFavoriteMovie = view.findViewById(R.id.editTextFavoriteMovie);
        editTextFavoriteAnimal = view.findViewById(R.id.editTextFavoriteAnimal);
        editTextLocation = view.findViewById(R.id.editTextLocation);
        buttonSaveProfile = view.findViewById(R.id.buttonSaveProfile);

        buttonSaveProfile.setOnClickListener(v -> saveProfileData());

        loadProfileData();
    }

    private void loadProfileData() {
        editTextName.setText(sharedPreferences.getString(KEY_NAME, ""));
        editTextBirthDate.setText(sharedPreferences.getString(KEY_BIRTH_DATE, ""));
        editTextFavoriteMovie.setText(sharedPreferences.getString(KEY_MOVIE, ""));
        editTextFavoriteAnimal.setText(sharedPreferences.getString(KEY_ANIMAL, ""));
        editTextLocation.setText(sharedPreferences.getString(KEY_LOCATION, ""));
    }

    private void saveProfileData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_NAME, editTextName.getText().toString());
        editor.putString(KEY_BIRTH_DATE, editTextBirthDate.getText().toString());
        editor.putString(KEY_MOVIE, editTextFavoriteMovie.getText().toString());
        editor.putString(KEY_ANIMAL, editTextFavoriteAnimal.getText().toString());
        editor.putString(KEY_LOCATION, editTextLocation.getText().toString());

        editor.apply();

        Toast.makeText(getContext(), "Профиль успешно сохранен!", Toast.LENGTH_SHORT).show();
    }
}