package ru.mirea.ostrovskiy.mireaproject.joke;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.ostrovskiy.mireaproject.R;
import ru.mirea.ostrovskiy.mireaproject.network.Joke;
import ru.mirea.ostrovskiy.mireaproject.network.JokeApiService;
import ru.mirea.ostrovskiy.mireaproject.network.RetrofitClient;

public class JokeFragment extends Fragment {

    private TextView textViewSetup, textViewPunchline;
    private Button buttonRefreshJoke;
    private JokeApiService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_joke, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewSetup = view.findViewById(R.id.textViewSetup);
        textViewPunchline = view.findViewById(R.id.textViewPunchline);
        buttonRefreshJoke = view.findViewById(R.id.buttonRefreshJoke);

        apiService = RetrofitClient.getRetrofitInstance().create(JokeApiService.class);

        buttonRefreshJoke.setOnClickListener(v -> loadJoke());

        loadJoke();
    }

    private void loadJoke() {
        textViewSetup.setText("Загрузка...");
        textViewPunchline.setText("");

        apiService.getRandomJoke().enqueue(new Callback<Joke>() {
            @Override
            public void onResponse(Call<Joke> call, Response<Joke> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Joke joke = response.body();
                    textViewSetup.setText(joke.setup);
                    textViewPunchline.setText(joke.punchline);
                } else {
                    textViewSetup.setText("Не удалось загрузить шутку");
                }
            }

            @Override
            public void onFailure(Call<Joke> call, Throwable t) {
                textViewSetup.setText("Ошибка сети: " + t.getMessage());
            }
        });
    }
}