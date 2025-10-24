package ru.mirea.ostrovskiy.mireaproject.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JokeApiService {
    @GET("random_joke")
    Call<Joke> getRandomJoke();
}