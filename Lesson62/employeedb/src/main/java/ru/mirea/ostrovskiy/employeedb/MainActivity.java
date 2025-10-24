package ru.mirea.ostrovskiy.employeedb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "DATABASE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "superhero-database")
                .allowMainThreadQueries()
                .build();

        SuperheroDao superheroDao = db.superheroDao();

        Superhero hero1 = new Superhero();
        hero1.name = "Бэтмен";
        hero1.power = 90;

        Superhero hero2 = new Superhero();
        hero2.name = "Супермен";
        hero2.power = 100;

        superheroDao.insert(hero1);
        superheroDao.insert(hero2);

        List<Superhero> superheroes = superheroDao.getAll();

        Log.d(TAG, "--- All Superheroes ---");
        for (Superhero hero : superheroes) {
            Log.d(TAG, "ID: " + hero.id + ", Name: " + hero.name + ", Power: " + hero.power);
        }


        Superhero superman = superheroDao.getById(2);
        superman.power = 110;
        superheroDao.update(superman);
        Log.d(TAG, "--- Updated Superman ---");
        Log.d(TAG, "ID: " + superman.id + ", Name: " + superman.name + ", Power: " + superman.power);
    }
}